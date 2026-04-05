package github.regularrabbit05.velocityAnonymousHider;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.List;
import java.util.Optional;

@Plugin(id = "velocityanonymoushider", name = "VelocityAnonymousHider", version = "1.0-SNAPSHOT", description = "Hide anonymous players from the server list", url = "https://regdev.me", authors = {"RegularRabbit05"})
public class ProxyPlugin {
    private boolean isPublic(ServerPing.SamplePlayer player) {
        if (player.getId().getLeastSignificantBits() != 0 || player.getId().getMostSignificantBits() != 0L) return true;
        return !player.getName().equals("Anonymous Player");
    }

    @Subscribe(priority = 1)
    public void onProxyPing(ProxyPingEvent event) {
        final ServerPing oldPing = event.getPing();
        final Optional<ServerPing.Players> optionalPlayers = oldPing.getPlayers();
        if (optionalPlayers.isEmpty()) return;
        final ServerPing.Players players = optionalPlayers.get();
        if (players.getSample().isEmpty()) return;
        final List<ServerPing.SamplePlayer> sample = players.getSample().stream().filter(this::isPublic).toList();
        event.setPing(oldPing.asBuilder().clearSamplePlayers().samplePlayers(sample).build());
    }
}
