package net.aoba.gui;

import java.util.ArrayList;
import java.util.List;
import net.aoba.Aoba;
import net.aoba.AobaClient;
import net.aoba.event.events.LeftMouseDownEvent;
import net.aoba.event.listeners.LeftMouseDownListener;
import net.aoba.misc.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public class NavigationBar implements LeftMouseDownListener {
	MinecraftClient mc = MinecraftClient.getInstance();

	private List<Page> options;
	private int selectedIndex;

	public NavigationBar() {
		options = new ArrayList<Page>();
		Aoba.getInstance().eventManager.AddListener(LeftMouseDownListener.class, this);
	}

	public void addPane(Page pane) {
		options.add(pane);
	}
	
	public List<Page> getPanes(){
		return this.options;
	}
	
	public int getSelectedIndex() {
		return this.selectedIndex;
	}
	
	public Page getSelectedPage() {
		return options.get(selectedIndex);
	}
	
	public void setSelectedIndex(int index) {
		if(index <= this.options.size()) {
			this.options.get(selectedIndex).setVisible(false);
			this.selectedIndex = index;
			this.options.get(selectedIndex).setVisible(true);
		}
	}
	
	public void update() {
		if(options.size() > 0) {
			options.get(selectedIndex).update();
		}
	}

	public void draw(DrawContext drawContext, float partialTicks, Color color) {
		Window window = mc.getWindow();

		int centerX = (window.getWidth() / 2);

		MatrixStack matrixStack = drawContext.getMatrices();

		int width = 100 * options.size();
		
		GuiManager hudManager = Aoba.getInstance().hudManager;
		RenderUtils.drawRoundedBox(matrixStack, centerX - (width / 2), 25, width, 25, 6, hudManager.backgroundColor.getValue());
		RenderUtils.drawRoundedOutline(matrixStack, centerX -  (width / 2), 25, width, 25, 6, hudManager.borderColor.getValue());

		RenderUtils.drawRoundedBox(drawContext.getMatrices(), centerX - (width / 2) + (100 * this.selectedIndex), 25, 100, 25, 5, new Color(150, 150, 150, 100));
			
		for(int i = 0; i < options.size(); i++) {
			Page pane = options.get(i);
			if(i == selectedIndex) {
				pane.render(drawContext, partialTicks, color);
			}
			RenderUtils.drawString(drawContext, pane.title, centerX - (width / 2) + 50 + (100 * i) - mc.textRenderer.getWidth(pane.title), 30, color);
		}
	}

	@Override
	public void OnLeftMouseDown(LeftMouseDownEvent event) {
		AobaClient aoba = Aoba.getInstance();
		Window window = mc.getWindow();
		
		double mouseX = event.GetMouseX();
		double mouseY = event.GetMouseY();
		int width = 100 * options.size();
		int centerX = (window.getWidth() / 2);
		int x = centerX - (width / 2);
		
		if (aoba.hudManager.isClickGuiOpen() && GuiManager.currentGrabbed == null) {
			if (mouseX >= (x) && mouseX <= (x + width)) {
				if (mouseY >= (25) && mouseY <= (50)) {
					int mouseXInt = (int) mouseX;
					int selection = (mouseXInt - x) / 100; 
					this.setSelectedIndex(selection);
				}
			}
		}
	}
}