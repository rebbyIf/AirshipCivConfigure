package com.rebby.airship_civ_configure;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.ShipForcesInducer;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;
import org.valkyrienskies.eureka.EurekaConfig;

public class ShipDragInducer implements ShipForcesInducer {

    public static void attachDragInducer(ServerShip ship) {
        ship.saveAttachment(ShipDragInducer.class, new ShipDragInducer());
    }

    @Override
    public void applyForces(@NotNull PhysShip physShip) {
        Vector3dc vel = ((PhysShipImpl)physShip).getPoseVel().getVel();
        double scaledMass = ((PhysShipImpl)physShip).getInertia().getShipMass() * EurekaConfig.SERVER.getSpeedMassScale();
        physShip.applyInvariantForce(vel.mul(scaledMass * -1, new Vector3d()));
    }
}
