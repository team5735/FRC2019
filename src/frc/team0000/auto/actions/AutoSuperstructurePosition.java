package frc.team0000.auto.actions;

import frc.team0000.states.SuperstructureConstants;
import frc.team0000.states.SuperstructureState;
import frc.team0000.subsystems.CheesyVision2;
import frc.team0000.subsystems.Superstructure;
import frc.lib.util.Util;
import edu.wpi.first.wpilibj.Timer;

public class AutoSuperstructurePosition implements Action {
    private static final CheesyVision2 cheesyVision2 = CheesyVision2.getInstance();
    private static final Superstructure superStructure = Superstructure.getInstance();
    private static final double kHeightEpsilon = 2.0;
    private static final double kAngleEpsilon = 5.0;
    private static final double kTimeout = 4.0;

    private final int mNumCubes;
    private double mHeight;
    private final double mAngle;
    private final boolean mWaitForCompletion;
    private final boolean mUseKickstand;
    private double mStartTime;
    private double mMinHeight = 0.0;

    public AutoSuperstructurePosition(int numCubes, double angle, boolean waitForCompletion, boolean useKickstand, double minHeight) {
        this(numCubes, angle, waitForCompletion, useKickstand);
        mMinHeight = minHeight;
    }

    public AutoSuperstructurePosition(int numCubes, double angle, boolean waitForCompletion, boolean useKickstand) {
        mNumCubes = numCubes;
        mAngle = angle;
        mWaitForCompletion = waitForCompletion;
        mUseKickstand = useKickstand;
    }

    @Override
    public void start() {
        double cheesyVisionHeight = cheesyVision2.getDesiredHeight((mAngle == SuperstructureConstants.kScoreBackwardsAngle), mNumCubes, mUseKickstand);
        if (cheesyVisionHeight < mMinHeight) {
            mHeight = mMinHeight;
        } else {
            mHeight = cheesyVisionHeight;
        }
//        superStructure.setDesiredHeight(mHeight);
//        superStructure.setDesiredAngle(mAngle);
        mStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void update() {
        double cheesyVisionHeight = cheesyVision2.getDesiredHeight((mAngle == SuperstructureConstants.kScoreBackwardsAngle), mNumCubes, mUseKickstand);
        if (cheesyVisionHeight < mMinHeight) {
            mHeight = mMinHeight;
        } else {
            mHeight = cheesyVisionHeight;
        }
//        superStructure.setDesiredHeight(mHeight);
    }

    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() - mStartTime > kTimeout) {
            System.out.println("Auto Superstructure Position timed out!!!");
            return true;
        }
        if (mWaitForCompletion) {
            SuperstructureState state = superStructure.getObservedState();
            return Util.epsilonEquals(state.height, mHeight, kHeightEpsilon) &&
                    Util.epsilonEquals(state.angle, mAngle, kAngleEpsilon);
        } else {
            return true;
        }
    }

    @Override
    public void done() {
        System.out.println("Auto Superstructure Position action finished");
    }
}
