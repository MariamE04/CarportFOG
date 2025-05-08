package app.util;

import app.entities.Carport;
import app.entities.Materials;
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
        List<Materials> beams = beamCalculator(length);
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

    public List<Materials> beamCalculator(int length) throws DatabaseException {
        List<Materials> beamsList = new ArrayList<>();
        Materials currentBeam = null;
        boolean lengthExceeded = false;
        List<Materials> allMaterials = MaterialMapper.getMaterialsByLengths();
        for (Materials materials : allMaterials) {
            if (materials.getLength() >= length && materials.getLength() < currentBeam.getLength()) {
                currentBeam = materials;
            }
        }
        if (currentBeam == null) {
            lengthExceeded = true;
            for (Materials materials : allMaterials) {
                if (materials.getLength() >= length/2 && materials.getLength() < currentBeam.getLength()) {
                    currentBeam = materials;
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
