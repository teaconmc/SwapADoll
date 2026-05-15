package com.github.tartaricacid.swapadoll.block;

import com.github.tartaricacid.swapadoll.blockentity.PlayerDollBlockEntity;
import com.github.tartaricacid.swapadoll.client.gui.ScreenProxy;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class PlayerDollBlock extends Block implements EntityBlock {
    public static final VoxelShape BLOCK_AABB = Block.box(2, 0, 2, 14, 14, 14);
    public static final MapCodec<PlayerDollBlock> CODEC = simpleCodec(PlayerDollBlock::new);

    public static final int MAX = RotationSegment.getMaxSegmentIndex();
    public static final int ROTATIONS = MAX + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public PlayerDollBlock(Properties properties) {
        super(properties);
        BlockState state = this.stateDefinition.any()
                .setValue(ROTATION, 0);
        this.registerDefaultState(state);
    }

    public PlayerDollBlock(Identifier id) {
        Properties properties = Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, id))
                .sound(SoundType.WOOL)
                .strength(0.5f)
                .noOcclusion();
        this(properties);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide() && level.getBlockEntity(pos) instanceof PlayerDollBlockEntity blockEntity && player.isCreative() && !player.isSecondaryUseActive()) {
            ScreenProxy.openPlayerDollScreen(blockEntity);
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (player.isCreative() && player.isSecondaryUseActive() && level.getBlockEntity(pos) instanceof PlayerDollBlockEntity blockEntity) {
            PlayerDollBlockEntity.Pose next = blockEntity.getPose().next();
            blockEntity.setPose(next);
            return InteractionResult.SUCCESS;
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        if (stateForPlacement == null) {
            return null;
        }
        int rot = RotationSegment.convertToSegment(context.getRotation());
        return stateForPlacement.setValue(ROTATION, rot);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), ROTATIONS));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ROTATION);
    }

    @Override
    protected MapCodec<PlayerDollBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PlayerDollBlockEntity(blockPos, blockState);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
        ItemStack stack = super.getCloneItemStack(level, pos, state, includeData, player);
        if (level.getBlockEntity(pos) instanceof PlayerDollBlockEntity blockEntity) {
            stack.set(DataComponents.PROFILE, blockEntity.getProfile());
        }
        return stack;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BLOCK_AABB;
    }
}
