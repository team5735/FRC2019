/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ElevatorJoystick extends Command {

  public ElevatorJoystick() {
    requires(Robot.elevator);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    // double rightTriggerValue = Robot.oi.subsystemController.triggers.getRight();
    // double leftTriggerValue = Robot.oi.subsystemController.triggers.getLeft();
    double input = Robot.oi.subsystemController.rightStick.getYCubed();

    double deltaPosition;
    if (input > 0.07) {
      deltaPosition = input;
    } else if (input < -0.07) {
      deltaPosition = input;
    } else {
      deltaPosition = 0;
    }

    Robot.elevator.setTargetPosition(Robot.elevator.getTargetPosition() + 0.4 * deltaPosition);
    Robot.elevator.updateMotionMagic();

    System.out.println("T:" + (Robot.elevator.getTargetPosition() + "     ").substring(0, 6) + Robot.elevator.encoderTicksToElevatorInches(Robot.elevator.getSensorPosition()) + " V: "
        + Robot.elevator.getMotorOutputPercent());
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
