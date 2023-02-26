package com.alrex.parcool.common.action.impl;

import com.alrex.parcool.client.animation.impl.JumpFromBarAnimator;
import com.alrex.parcool.client.input.KeyRecorder;
import com.alrex.parcool.common.action.Action;
import com.alrex.parcool.common.action.StaminaConsumeTiming;
import com.alrex.parcool.common.capability.IStamina;
import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.common.capability.impl.Parkourability;
import com.alrex.parcool.utilities.EntityUtil;
import net.minecraft.world.entity.player.Player;

import java.nio.ByteBuffer;

;

public class JumpFromBar extends Action {
	@Override
	public boolean canStart(Player player, Parkourability parkourability, IStamina stamina, ByteBuffer startInfo) {
		HangDown hangDown = parkourability.get(HangDown.class);
		return hangDown.isDoing()
				&& hangDown.getDoingTick() > 2
				&& parkourability.getActionInfo().can(JumpFromBar.class)
				&& KeyRecorder.keyJumpState.isPressed();
	}

	@Override
	public boolean canContinue(Player player, Parkourability parkourability, IStamina stamina) {
		return getDoingTick() < 2;
	}

	@Override
	public void onStartInLocalClient(Player player, Parkourability parkourability, IStamina stamina, ByteBuffer startData) {
		EntityUtil.addVelocity(player, player.getLookAngle().multiply(1, 0, 1).normalize().scale(player.getBbWidth() * 0.75));
		Animation animation = Animation.get(player);
		if (animation != null) animation.setAnimator(new JumpFromBarAnimator());
	}

	@Override
	public void onStartInOtherClient(Player player, Parkourability parkourability, ByteBuffer startData) {
		Animation animation = Animation.get(player);
		if (animation != null) animation.setAnimator(new JumpFromBarAnimator());
	}

	@Override
	public StaminaConsumeTiming getStaminaConsumeTiming() {
		return StaminaConsumeTiming.OnStart;
	}
}