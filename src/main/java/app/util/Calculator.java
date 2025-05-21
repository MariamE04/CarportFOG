package app.util;


import app.entities.Material;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.MaterialMapper;

import java.util.ArrayList;
import java.util.List;

public class Calculator {
    private static ConnectionPool connectionPool;

    public static List<Material> orderCalculator(int width, int length) throws DatabaseException {
        List<Material> materials = new ArrayList<>();
        int postCount = postCounter(width);
        for (int i = 0; i < postCount; i++)
            materials.add(MaterialMapper.getPost());
        int rafterCount = rafterCalculator(width);
        for (int i = 0; i < rafterCount; i += 15)
            materials.add(MaterialMapper.getRafter());
        materials.addAll(beamCalculator(width));
        return materials;
    }

    public static CarportSvg svgCalculator(int width, int length) throws DatabaseException {
        int postCount = postCounter(width);
        int postSpace = postSpace(width);
        int rafterCount = rafterCalculator(width);
        List<Material> beams = beamCalculator(width);
        return new CarportSvg(width, length, postCount, postSpace, rafterCount, beams);
    }

    private static int postCounter(int width){
        int count = 4;
        while (width - 130 > 340){
            count += 2;
            width -= 340;
        }
        return count;
    }

    private static int postSpace(int width){
        int postCount = postCounter(width);
        int space = (width - 130)/(postCount/2) - 1;
        return space;
    }

    private static int rafterCalculator(int width){
        int count = 2;
        while (width > 60){
            count += 1;
            width -= 60;
        }
        return count;
    }


    private static List<Material> beamCalculator(int width) throws DatabaseException {
        List<Material> beamsList = new ArrayList<>();
        boolean lengthExceeded = true;
        List<Material> allMaterials = MaterialMapper.getBeamsByLengths();
        Material currentBeam = allMaterials.get(0);
        for (Material material : allMaterials) {
            if (material.getLength() >= width && material.getLength() <= currentBeam.getLength()) {
                currentBeam = material;
                lengthExceeded = false;
            }
        }
        if (lengthExceeded){
            for (Material material : allMaterials) {
                if (material.getLength() >= width /2 && material.getLength() <= currentBeam.getLength()) {
                    currentBeam = material;
                }
            }
        }
        beamsList.add(currentBeam);
        beamsList.add(currentBeam);
        if (lengthExceeded) {
            beamsList.add(currentBeam);
            beamsList.add(currentBeam);
        }
        return  beamsList;
    }

    public static void setConnectionPool(ConnectionPool newConnectionPool) {
        connectionPool = newConnectionPool;
    }
}
