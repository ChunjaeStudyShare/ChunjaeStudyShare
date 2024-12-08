package net.fullstack7.studyShare.util;

public class QueryUtil {
    public static String generateSortQuery(String sortType, String sortOrder) {
        if (sortType == null || sortType.trim().isEmpty()) {
            return "";
        }
        String direction = (sortOrder != null && sortOrder.equalsIgnoreCase("DESC")) ? "DESC" : "ASC";
        return "ORDER BY " + sortType + " " + direction;
    }
}
