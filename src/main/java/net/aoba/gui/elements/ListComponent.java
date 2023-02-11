package net.aoba.gui.elements;

import java.util.ArrayList;
import java.util.List;

import net.aoba.gui.ClickGuiTab;
import net.aoba.gui.Color;
import net.aoba.gui.HudManager;
import net.aoba.settings.ListSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ListComponent extends Component {

	private String text;
	private boolean wasClicked = false;
	private ListSetting list;
	private List<Component> settingsList = new ArrayList<Component>();
	
	public ListComponent(String text, ClickGuiTab parent) {
		super();
		this.text = text;
		this.parent = parent;
	}

	public ListComponent(ClickGuiTab parent, ListSetting list) {
		super();
		this.text = list.getName();
		this.parent = parent;
		this.list = list;
	}

	@Override
	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if (HudManager.currentGrabbed == null) {
			if (mouseClicked) {
				if (mouseY >= (((parentY + offset + 4))) && mouseY <= (parentY + offset + 22)) {
					// If Left arrow clicked.
					if (mouseX >= ((parentX + 4)) && mouseX <= ((parentX + 64))) {
						if (!this.wasClicked) {
							list.decrement();
							this.wasClicked = true;
							return;
						}
					}
					// If Right arrow clicked.
					if (mouseX >= ((parentX + parentWidth - 64)) && mouseX <= ((parentX + parentWidth - 4))) {
						if (!this.wasClicked) {
							list.increment();
							this.wasClicked = true;
							return;
						}
					}
				}
			} else {
				if (this.wasClicked) {
					this.wasClicked = false;
				}
			}
		}
	}

	@Override
	public void draw(int offset, MatrixStack matrixStack, float partialTicks, Color color) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		int length = MinecraftClient.getInstance().textRenderer.getWidth(list.getValue()); 
		renderUtils.drawOutlinedBox(matrixStack, parentX + 4, parentY + offset, parentWidth - 8, 22, new Color(25,25,25),
				0.3f);
		renderUtils.drawStringWithScale(matrixStack, list.getValue(), (parentX + (parentWidth / 2)) - length , parentY + offset + 4, 0xFFFFFF);
		renderUtils.drawStringWithScale(matrixStack,"<<", parentX + 8, parentY + offset + 4, 0xFFFFFF);
		renderUtils.drawStringWithScale(matrixStack,">>", parentX + 8 + (parentWidth - 34), parentY + offset + 4, 0xFFFFFF);
	}
}