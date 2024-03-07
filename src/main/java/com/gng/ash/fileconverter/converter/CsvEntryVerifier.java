package com.gng.ash.fileconverter.converter;

import com.gng.ash.fileconverter.model.CsvEntry;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class CsvEntryVerifier implements BeanVerifier<CsvEntry> {
    private static final String idRegex = "^[0-9]X[0-9]D[0-9]*$";
    private static final Pattern idPattern = Pattern.compile(idRegex);

    private static final String uuidRegex = "^[{]?[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}[}]?$";
    private static final Pattern uuidPattern = Pattern.compile(uuidRegex);

    private static final String driveRegex = "^(Drives|Rides)[A-Za-z ]*?$";
    private static final Pattern drivePattern = Pattern.compile(driveRegex);

    private static final String likeRegex = "^Likes [A-Za-z]*?$";
    private static final Pattern likePattern = Pattern.compile(likeRegex);

    @Override
    public boolean verifyBean(CsvEntry csvEntry) throws CsvConstraintViolationException {
        uuIdVerifier(csvEntry.getUuId());
        idVerifier(csvEntry.getId());
        nameVerifier(csvEntry.getName());
        transportVerifier(csvEntry.getTransport());
        likesVerifier(csvEntry.getLikes());
        avgSpeedVerifier(csvEntry.getAvgSpeed());
        topSpeedVerifier(csvEntry.getTopSpeed());

        return true;
    }

    private static void idVerifier(String id) throws CsvConstraintViolationException {
        Matcher idMatcher = idPattern.matcher(id);
        if (!idMatcher.matches()) {
            throw new CsvConstraintViolationException("Invalid id: " + id);
        }
    }

    private void uuIdVerifier(String uuid) throws CsvConstraintViolationException {
        Matcher uuidMatcher = uuidPattern.matcher(uuid);
        if (!uuidMatcher.matches()) {
            throw new CsvConstraintViolationException("Invalid uuid: " + uuid);
        }
    }

    private void transportVerifier(String transport) throws CsvConstraintViolationException {
        Matcher uuidMatcher = drivePattern.matcher(transport);
        if (!uuidMatcher.matches()) {
            throw new CsvConstraintViolationException("Invalid transport: " + transport);
        }
    }

    private void likesVerifier(String likes) throws CsvConstraintViolationException {
        Matcher uuidMatcher = likePattern.matcher(likes);
        if (!uuidMatcher.matches()) {
            throw new CsvConstraintViolationException("Invalid likes: " + likes);
        }
    }

    private void nameVerifier(String name) throws CsvConstraintViolationException {
        if (name.isBlank() || name.length() > 100 || name.contains("=")) {
            throw new CsvConstraintViolationException("Invalid likes: " + name);
        }
    }

    private void avgSpeedVerifier(double speed) throws CsvConstraintViolationException {
        if (speed <= 0 || speed > 70) {
            throw new CsvConstraintViolationException("Invalid average speed: " + speed);
        }
    }

    private void topSpeedVerifier(double speed) throws CsvConstraintViolationException {
        if (speed <= 0 || speed > 250) {
            throw new CsvConstraintViolationException("Invalid top speed: " + speed);
        }
    }

}
