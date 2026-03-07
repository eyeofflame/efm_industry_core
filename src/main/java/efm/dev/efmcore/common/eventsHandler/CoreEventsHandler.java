package efm.dev.efmcore.common.eventsHandler;

import efm.dev.efmcore.Efmcore;
import efm.dev.efmcore.common.registry.EfmModRegistry;
import efm.dev.efmcore.common.untils.EfmHelper;
import efm.dev.efmcore.common.untils.bossesHealthRange.BossesHealthRange;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundUpdateAttributesPacket;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Efmcore.MODID)
public class CoreEventsHandler {
    @SubscribeEvent
    public static void repairAnvil(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().level().isClientSide) {
            Player player = event.getEntity();
            ItemStack stack = event.getItemStack();
            if (stack.is(Items.IRON_BLOCK) && repairTestingAnvil(event.getLevel(), event.getLevel().getBlockState(event.getPos()), event.getPos(), player)) {
                event.getLevel().playSound(null, event.getPos(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1f, 1f);
                stack.shrink(1);
                event.setCanceled(true);
            }
        }
    }

    private static boolean repairTestingAnvil(Level level, BlockState state, BlockPos pos, Player player) {
        if (player.isCrouching()) return false;
        if (state.is(Blocks.DAMAGED_ANVIL)) {
            Direction property = state.getValue(HorizontalDirectionalBlock.FACING);
            level.setBlock(pos, Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, property), 2);
            return true;
        }
        if (state.is(Blocks.CHIPPED_ANVIL)) {
            Direction property = state.getValue(HorizontalDirectionalBlock.FACING);
            level.setBlock(pos, Blocks.ANVIL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, property), 2);
            return true;
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide) return;
        if (event.getEntity() instanceof Player player) {
            if (checkIsBossesThisClass(event.getSource(), player)) {
                EfmHelper.deleteItems(player);
            }
        }
        //Monster Respawn
        if (event.getEntity() instanceof Enemy && !checkIsBossesThisClass(event.getEntity())) {
            if (EfmHelper.efmRandomNoRepeat(100, Efmcore.configEfm.getMonsterRespawnProp()).contains(new Random().nextInt(100)) && !event.getEntity().getTags().contains("efm:had_res")) {
                Level level = event.getEntity().level();
                AABB searchArea = event.getEntity().getBoundingBox().inflate(30);
                List<Player> playerList = level.getEntitiesOfClass(Player.class, searchArea);

                LivingEntity enemy = event.getEntity();
                event.getEntity().setHealth(event.getEntity().getMaxHealth());
                event.getEntity().setAbsorptionAmount(event.getEntity().getMaxHealth() * 0.5f);
                enemy.addTag("efm:strength");
                enemy.addTag("efm:had_res");

                playerList.forEach(player -> {
                    player.sendSystemMessage(Component.literal("[").append(Component.translatable(event.getEntity().getName().getString())).append("] 我已归来！").withStyle(ChatFormatting.RED));
                    ((ServerPlayer) player).connection.send(new ClientboundUpdateAttributesPacket(
                            enemy.getId(),
                            enemy.getAttributes().getSyncableAttributes()
                    ));
                });

                ServerLevel serverLevel = (ServerLevel) level;
                UUID id = enemy.getUUID();
                serverLevel.getServer().tell(new TickTask(serverLevel.getServer().getTickCount() + 600,
                        () -> {
                            Entity entity = serverLevel.getEntity(id);
                            if (entity != null && entity.isAlive()) {
                                entity.removeTag("efm:strength");
                            }
                        }
                ));

                event.setCanceled(true);
            }
        } else if (checkIsBossesThisClass(event.getEntity())) {

            //boss死后爆种
            LivingEntity boss = event.getEntity();
            if (EfmHelper.random(40) && !boss.getTags().contains("efm:respawn")) {
                Level level = event.getEntity().level();
                AABB searchArea = event.getEntity().getBoundingBox().inflate(50);
                List<Player> playerList = level.getEntitiesOfClass(Player.class, searchArea);

                EfmHelper.reducePlayersDuriation(playerList, boss);
                playerList.forEach(player -> {
                    player.sendSystemMessage(Component.translatable(boss.getName().getString()).append(" 进入了第二阶段！").withStyle(ChatFormatting.RED));
                });


                boss.addTag("efm:respawn");
                event.setCanceled(true);
            }
        }
    }

    private static boolean checkIsBossesThisClass(DamageSource source, Player player) {
        return (player.getLastHurtByMob() != null && (player.getLastHurtByMob().getType().is(Tags.EntityTypes.BOSSES) || EfmHelper.checkIsBoss(player.getLastHurtByMob()))) || (source.getEntity() != null && (source.getEntity().getType().is(Tags.EntityTypes.BOSSES) || EfmHelper.checkIsBoss(source.getEntity())));
    }

    private static boolean checkIsBossesThisClass(Entity entity) {
        return entity.getType().is(Tags.EntityTypes.BOSSES) || EfmHelper.checkIsBoss(entity);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide) return;

        if (event.getEntity() instanceof Player player) {
            if (event.getSource().getEntity() instanceof LivingEntity entity && entity.getTags().contains("efm:strength")) {
                event.setAmount(event.getAmount() * 1.5f);
            } else if (event.getSource().getEntity() instanceof LivingEntity entity && entity.getTags().contains("efm:respawn")) {
                event.setAmount(event.getAmount() * 2f);
            }
        }

        if (event.getEntity().getTags().contains("efm:strenth")) {
            event.setAmount(event.getAmount() * 0.8f);
        }

        //boss护盾需特定物品破
        if (checkIsBossesThisClass(event.getEntity())) {
            LivingEntity entity = event.getEntity();
            if (entity.getAbsorptionAmount() > 0) {

                MobEffectInstance instance = entity.getEffect(EfmModRegistry.CURSE_EFM.get());

                if (instance == null) {
                    event.setAmount(0f);
                }

            } else if (event.getEntity().getAbsorptionAmount() <= 0) {
                float damage = event.getAmount();
                event.setAmount(damage * 0.7f);
            }
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(TickEvent.LevelTickEvent event) {
        if (!event.level.isClientSide() && event.level instanceof ServerLevel level && level.dimension() == Level.END && event.phase == TickEvent.Phase.END && event.level.getServer().getTickCount() % 60 == 0) {
            WorldBorder worldBorder = level.getWorldBorder();
            worldBorder.setCenter(0, 0);
            if (level.getDragonFight() != null && !level.getDragonFight().hasPreviouslyKilledDragon()) {
                worldBorder.setWarningBlocks(5);
                worldBorder.setSize(600d);
                worldBorder.setDamagePerBlock(5d);
                worldBorder.setDamageSafeZone(0d);
            } else if (level.getDragonFight() != null && level.getDragonFight().hasPreviouslyKilledDragon()) {
                worldBorder.setSize(WorldBorder.MAX_SIZE);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            if (event.player.level().dimension() == Level.END) {
                WorldBorder worldBorder = event.player.level().getWorldBorder();
                if (isPlayerNearBorder(worldBorder, event.player.blockPosition(), worldBorder.getWarningBlocks())) {
                    event.player.displayClientMessage(Component.literal("完成该存档末影龙首杀后方可前往外岛！").withStyle(ChatFormatting.RED), true);
                }
            }

            if (event.player.tickCount % 200 == 0) {
                event.player.setAbsorptionAmount(event.player.getMaxHealth() * (Efmcore.configEfm.getPlayerGetProtection() / 100f));
                ((ServerPlayer) event.player).connection.send(
                        new ClientboundUpdateAttributesPacket(
                                event.player.getId(),
                                event.player.getAttributes().getSyncableAttributes()
                        )
                );
            }


            //double jump
            if (!event.player.getPersistentData().contains("efm:jump") || event.player.onGround()) {
                event.player.getPersistentData().putInt("efm:jump", 0);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide || !checkIsBossesThisClass(event.getEntity()) || event.getEntity().tickCount % 100 != 0)
            return;

        LivingEntity entity = event.getEntity();

        if (entity.getMaxHealth() != entity.getHealth() && entity.getEffect(EfmModRegistry.CURSE_EFM.get()) == null) {
            entity.heal(entity.getMaxHealth() * 0.05f);
        }
    }

    private static boolean isPlayerNearBorder(WorldBorder border, BlockPos playerPos, double warningDistance) {
        // 如果玩家不在边界内，无需警告（或者你可以根据需求处理）
        if (!border.isWithinBounds(playerPos)) {
            return false;
        }

        // 获取边界的核心参数：中心点坐标和半边长
        double centerX = border.getCenterX();
        double centerZ = border.getCenterZ();
        double halfSize = border.getSize() / 2;

        // 计算边界正方形的四个边界坐标
        double minX = centerX - halfSize;
        double maxX = centerX + halfSize;
        double minZ = centerZ - halfSize;
        double maxZ = centerZ + halfSize;

        double playerX = playerPos.getX();
        double playerZ = playerPos.getZ();

        // 计算到四条边的距离
        double distanceToMinX = Math.abs(playerX - minX);
        double distanceToMaxX = Math.abs(playerX - maxX);
        double distanceToMinZ = Math.abs(playerZ - minZ);
        double distanceToMaxZ = Math.abs(playerZ - maxZ);

        // 找出到任意一条边的最近距离
        double minDistance = Math.min(Math.min(distanceToMinX, distanceToMaxX),
                Math.min(distanceToMinZ, distanceToMaxZ));

        // 判断是否小于警告阈值
        return minDistance < warningDistance;
    }

    @SubscribeEvent
    public static void onLivingJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            if (EfmHelper.checkIsMaxHealthBoss(event.getEntity())) {
                Random random = new Random();

                for (BossesHealthRange range : Efmcore.configEfm.getBossesMaxHealthRange()) {
                    if (range.id.equals(ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType()).toString())) {
                        int maxHealth = random.nextInt(range.max - range.min) + range.min;
                        if (event.getEntity() instanceof LivingEntity entity) {
                            entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
                            entity.setHealth(maxHealth);
                        }
                        break;
                    }
                }
            }

            if (checkIsBossesThisClass(event.getEntity())) {
                LivingEntity entity = (LivingEntity) event.getEntity();
                entity.setAbsorptionAmount(entity.getMaxHealth() * (Efmcore.configEfm.getBossesGetProtection() / 100f));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityMount(EntityMountEvent event) {
        if (event.getLevel().isClientSide) return;
        if (event.getEntityMounting() instanceof Enemy && event.getEntityBeingMounted() instanceof Boat) {
            event.setCanceled(true);
        }
    }
}
