package me.tecnio.ahm.check.api;

import lombok.Getter;
import lombok.SneakyThrows;
import me.tecnio.ahm.AHM;
import me.tecnio.ahm.check.Check;
import me.tecnio.ahm.check.api.annotations.Disabled;
import me.tecnio.ahm.check.impl.speed.SpeedA;
import me.tecnio.ahm.check.impl.speed.SpeedB;
import me.tecnio.ahm.data.PlayerData;
import me.tecnio.ahm.util.ClassUtils;
import org.atteo.classindex.ClassIndex;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class CheckManager {

    private final List<Class<?>> checks = new ArrayList<>();
    private final List<Constructor<?>> constructors = new ArrayList<>();

    public CheckManager() {
        for (Class<?> clazz : ClassUtils.getClassesInPackage(AHM.get().getPlugin(), "me.tecnio.ahm.check.impl")) {

            if (!Check.class.isAssignableFrom(clazz)) {
                continue;
            }

            try {
                this.checks.add(clazz);
                this.constructors.add(clazz.getConstructor(PlayerData.class));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        Bukkit.getConsoleSender().sendMessage("Added Checks " + checks.size());
    }

    @SneakyThrows
    public List<Check> loadChecks(final PlayerData data) {
        final List<Check> checkList = new ArrayList<>();

        for (final Constructor<?> constructor : this.constructors) {
            checkList.add((Check) constructor.newInstance(data));
        }
        Bukkit.getConsoleSender().sendMessage("Checks debloat. " + checkList.size());

        return checkList;
    }
}