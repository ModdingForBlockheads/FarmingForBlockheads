package net.blay09.mods.farmingforblockheads.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MerchantGoal extends Goal {

    private final MerchantEntity entity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;

    public MerchantGoal(MerchantEntity entity, double movementSpeed) {
        this.entity = entity;
        this.movementSpeed = movementSpeed;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (entity.isAtMarket()) {
            entity.setToFacingAngle();
            return false;
        }

        BlockPos marketPos = entity.getMarketEntityPosition();
        if (marketPos != null && entity.getNavigation().isStableDestination(marketPos)) {
            this.movePosX = marketPos.getX() + 0.5;
            this.movePosY = marketPos.getY() + 1;
            this.movePosZ = marketPos.getZ() + 0.5;
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        entity.getNavigation().moveTo(movePosX, movePosY, movePosZ, movementSpeed);
    }

}
