package net.aoba.module.modules.combat;

import org.lwjgl.glfw.GLFW;

import net.aoba.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class CrystalAura extends Module {
	private float radius = 10.0f;

	public CrystalAura() {
		this.setName("CrystalAura");
		this.setBind(new KeyBinding("key.crystalaura", GLFW.GLFW_KEY_UNKNOWN, "key.categories.aoba"));
		this.setCategory(Category.Combat);
		this.setDescription("Attacks anything within your personal space.");
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onToggle() {

	}

	@Override
	public void onUpdate() {
		for (PlayerEntity player : MC.world.getPlayers()) {
			if (player == MC.player || MC.player.distanceTo(player) > this.radius) {
				continue;
			}
			BlockPos entityPos = player.getBlockPos();
			BlockPos check = entityPos.add(0, -1, 0);
			BlockState bs = MC.world.getBlockState(check);
			Block block = bs.getBlock();
			if (block != Blocks.OBSIDIAN && block != Blocks.BEDROCK)
				continue;

			System.out.println(player.getName().getString() + ": " + bs.getBlock().getName().getString());
			for (int slot = 0; slot < 9; slot++) {
				Item item = MC.player.getInventory().getStack(slot).getItem();
				if (item == Items.END_CRYSTAL) {
					MC.player.getInventory().selectedSlot = slot;
					break;
				}
			}
			//BlockHitResult rayTrace = new BlockHitResult(new Vec3d(0,0,0), Direction.UP, check, false);
			this.MC.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
		}

		// HIT THING
		for (Entity entity : MC.world.getEntities()) {
			if (MC.player.distanceTo(entity) < radius) {
				if (entity instanceof EndCrystalEntity) {
					MC.player.attack(entity);
				}
			}
		}

	}

	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {

	}

	@Override
	public void onSendPacket(Packet<?> packet) {

	}

	@Override
	public void onReceivePacket(Packet<?> packet) {

	}
}