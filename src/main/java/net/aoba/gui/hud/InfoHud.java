/*
* Aoba Hacked Client
* Copyright (C) 2019-2024 coltonk9043
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.aoba.gui.hud;

import org.joml.Matrix4f;

import net.aoba.gui.GuiManager;
import net.aoba.misc.RenderUtils;
import net.aoba.utils.types.Vector2;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class InfoHud extends AbstractHud {

	String positionText = "";
	String timeText = "";
	String fpsText = "";
	
	// 
	public InfoHud(int x, int y) {
		super("InfoHud", x, y, 190, 60);
	}
	
	@Override
	public void update() {
		MinecraftClient mc = MinecraftClient.getInstance();

		int time = ((int)mc.world.getTime() + 6000)% 24000;
		String suffix = time >= 12000 ? "PM" : "AM";
		String timeString = (time / 10) % 1200 + "";
		for (int n = timeString.length(); n < 4; ++n) {
			timeString = "0" + timeString;
        }
		final String[] strsplit = timeString.split("");
		String hours = strsplit[0] + strsplit[1];
		if(hours.equalsIgnoreCase("00")) {
			hours = "12";
		}
		final int minutes = (int)Math.floor(Double.parseDouble(strsplit[2] + strsplit[3]) / 100.0 * 60.0);
		String sm = minutes + "";
        if (minutes < 10) {
            sm = "0" + minutes;
        }
		timeString = hours + ":" + sm.charAt(0) + sm.charAt(1) + suffix;
		positionText = "XYZ: " + (int)mc.player.getBlockX() + ", " + (int)mc.player.getBlockY() + ", " + (int)mc.player.getBlockZ();
		timeText = "Time: " + timeString;
		fpsText = "FPS: " + mc.fpsDebugString.split(" ", 2)[0] + " Day: " + (int) (mc.world.getTime() / 24000);
		
		int newWidth = (int)(mc.textRenderer.getWidth(positionText) * 2) + 20;
		if(this.getWidth()!= newWidth) {
			if(newWidth >= 190) {
				this.setWidth(newWidth);
			}else {
				this.setWidth(190);
			}
		}
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		if(this.visible) {
			MatrixStack matrixStack = drawContext.getMatrices();
			Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
			
			// Draws background depending on components width and height
			Vector2 pos = position.getValue();
			
			RenderUtils.drawRoundedBox(matrix4f, pos.x, pos.y, width, height, 6, GuiManager.backgroundColor.getValue());
			RenderUtils.drawRoundedOutline(matrix4f, pos.x, pos.y, width, height, 6, GuiManager.borderColor.getValue());
			
			RenderUtils.drawString(drawContext, positionText, pos.x + 5, pos.y + 4, GuiManager.foregroundColor.getValue());
			RenderUtils.drawString(drawContext, timeText, pos.x + 5, pos.y + 24, GuiManager.foregroundColor.getValue());
			RenderUtils.drawString(drawContext, fpsText, pos.x + 5, pos.y + 44, GuiManager.foregroundColor.getValue());
		}
	}
}
