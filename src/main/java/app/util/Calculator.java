package app.util;

import app.entities.Carport;
import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private static ConnectionPool connectionPool;

    public void carportCalculator(Carport carport) throws DatabaseException {
        int length = carport.getLength();
        int postCount = postCalculator(length);
        int rafterCount = rafterCalculator(length);
        List<Material> beams = beamCalculator(length);
    }

    public int postCalculator(int length){
        int count = 4;
        while (length - 130 > 340){
            count += 2;
            length -= 340;
        }
        return count;
    }

    public int rafterCalculator(int length){
        int count = 2;
        while (length >= 55){
            count += 1;
            length -= 55;
        }
        return count;
    }

    public List<Material> beamCalculator(int length) throws DatabaseException {
        List<Material> beamsList = new ArrayList<>();
        Material currentBeam = null;
        boolean lengthExceeded = false;
        List<Material> allMaterials = MaterialMapper.getMaterialsByLengths();
        for (Material material : allMaterials) {
            if (material.getLength() >= length && material.getLength() < currentBeam.getLength()) {
                currentBeam = material;
            }
        }
        if (currentBeam == null) {
            lengthExceeded = true;
            for (Material material : allMaterials) {
                if (material.getLength() >= length/2 && material.getLength() < currentBeam.getLength()) {
                    currentBeam = material;
                }
            }
        }
        beamsList.add(currentBeam);
        if (lengthExceeded) {
            beamsList.add(currentBeam);
        }
        return  beamsList;
    }

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }
}
