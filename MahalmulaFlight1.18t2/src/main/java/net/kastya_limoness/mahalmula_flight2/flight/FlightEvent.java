package net.kastya_limoness.mahalmula_flight2.flight;

import net.kastya_limoness.mahalmula_flight2.entities.MahalmulaShipEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Random;

public abstract class FlightEvent {

    public static final FlightEvent[] EVENTS = new FlightEvent[] {new FuelEvent(), new NavigationEvent(), new FlintEvent()};
    public static FlightEvent randomEvent(Random random)
    {
        return EVENTS[random.nextInt(EVENTS.length)];
    }
    public abstract String getEventName();
    public abstract boolean acceptItem(ItemStack item);
    public abstract void onCreating(MahalmulaShipEntity ship);
    public abstract void onDisapearing(MahalmulaShipEntity ship);
}

class FuelEvent extends FlightEvent
{
    @Override
    public String getEventName() {
        return "fuel_event";
    }

    @Override
    public boolean acceptItem(ItemStack item) {
        return item.getItem().equals(Items.DRAGON_BREATH);
    }

    @Override
    public void onCreating(MahalmulaShipEntity ship) {

    }

    @Override
    public void onDisapearing(MahalmulaShipEntity ship) {
        ship.getEntityData().set(MahalmulaShipEntity.EFFECTS_PARAMETER, 2);
    }
}

class FlintEvent extends FlightEvent
{
    @Override
    public String getEventName() {
        return "flint_event";
    }

    @Override
    public boolean acceptItem(ItemStack item) {
        return item.getItem().equals(Items.FLINT_AND_STEEL);
    }

    @Override
    public void onCreating(MahalmulaShipEntity ship) {

    }

    @Override
    public void onDisapearing(MahalmulaShipEntity ship) {
        ship.getEntityData().set(MahalmulaShipEntity.EFFECTS_PARAMETER, 4);
    }
}

class NavigationEvent extends FlightEvent
{
    @Override
    public String getEventName() {
        return "navigation_event";
    }

    @Override
    public boolean acceptItem(ItemStack item) {
        return item.getItem().equals(Items.COMPASS);
    }

    @Override
    public void onCreating(MahalmulaShipEntity ship) {

    }

    @Override
    public void onDisapearing(MahalmulaShipEntity ship) {
        ship.getEntityData().set(MahalmulaShipEntity.EFFECTS_PARAMETER, 3);
    }
}

