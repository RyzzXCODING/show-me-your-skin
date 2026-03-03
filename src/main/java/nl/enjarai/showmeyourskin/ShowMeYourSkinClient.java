package nl.enjarai.showmeyourskin;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.render.RenderStateKey;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.showmeyourskin.client.ModKeyBindings;
import nl.enjarai.showmeyourskin.config.ArmorConfig;
import nl.enjarai.showmeyourskin.config.ModConfig;
import nl.enjarai.showmeyourskin.config.SyncedModConfig;
import nl.enjarai.showmeyourskin.gui.ArmorConfigScreen;
import nl.enjarai.showmeyourskin.net.SettingsUpdatePacket;
import org.jetbrains.annotations.Nullable;

public class ShowMeYourSkinClient implements ClientModInitializer, CicadaEntrypoint {
	public static final RenderStateKey<Entity> ENTITY_RENDER_STATE_KEY = RenderStateKey.of(
			ShowMeYourSkin.id("entity"),
			null
	);

	public static SyncedModConfig serverConfig = null;

	@Override
	public void onInitializeClient() {
		ModConfig.INSTANCE.ensureValid();

		ModKeyBindings.register();
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);

		initNetworking();
	}

	private void initNetworking() {
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			serverConfig = null;
		});
	}

	public static void syncToServer(ArmorConfig config) {
		if (!ShowMeYourSkin.NET_CHANNEL.canSendToServer()) {
			return;
		}

		var player = Minecraft.getInstance().player;
		if (player != null) {
			var component = player.getComponent(Components.ARMOR_CONFIG);
			component.setConfig(config);

			ShowMeYourSkin.NET_CHANNEL.clientHandle().send(new SettingsUpdatePacket(config));
		}
	}

	public static ArmorConfigScreen createConfigScreen(@Nullable Screen parent) {
		var player = Minecraft.getInstance().player;
		if (ShowMeYourSkin.NET_CHANNEL.canSendToServer() && player != null) {
			var component = player.getComponent(Components.ARMOR_CONFIG);
			return new ArmorConfigScreen(
				component.getConfig(), player, true,
				Component.translatable("gui.showmeyourskin.armorScreen.synced"), parent
			);
		} else {
			return new ArmorConfigScreen(
				ModConfig.INSTANCE.global, player, false,
				Component.translatable("gui.showmeyourskin.armorScreen.global"), parent
			);
		}
	}

	public void tick(Minecraft client) {
		ModKeyBindings.tick(client);
	}

	@Override
	public void registerConversations(ConversationManager conversationManager) {
		conversationManager.registerSource(
				JsonSource.fromUrl("https://raw.githubusercontent.com/enjarai/show-me-your-skin/master/src/main/resources/cicada/showmeyourskin/conversations.json")
						.or(JsonSource.fromResource("cicada/showmeyourskin/conversations.json")),
				ShowMeYourSkin.LOGGER::info
		);
	}
}
