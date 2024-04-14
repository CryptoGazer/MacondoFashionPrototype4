package com.macondo_cs.MacondoFashionPrototype4.models;

import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class ServiceFunctionality {
    final public static String[] clothesNames = {
            "Coats & Jackets",  // may be errors because of the spaces the string
            "Pullovers",
            "Shirts",
            "T-Shirts",
            "Hoodies",
            "Jeans",
            "Trousers",
            "Sport"
    };
    final public static String[] accessoriesNames = {
            "Watches",
            "Bracelets",
            "Souvenirs"
    };
    final public static String[] productsTableProps = {
        "productID",
        "name",
        "price",
        "category",
        "sex",
        "description",
        "quantity",
        "totalSold",
        "image"
    };

    final public static String[] usersTableProps = {
        "userID",
        "name",
        "email",
        "password",
        "datetimeRegirested",
        "totalBought",
        "totalSpent"
    };

    final public static String[] cartHeaders = {
        "Name",
        "Sum",
        "Quantity",
        "Price"
    };

    public static String getProductType(String productName) {
        if (productName.equals("coats-jackets")) {
            return "clothes";
        }
        for (String cl : clothesNames) {
            if (cl.toLowerCase().equals(productName))
                return "clothes";
        }
        for (String ac : accessoriesNames) {
            if (ac.toLowerCase().equals(productName))
                return "accessories";
        }
        return "";
    }

    public static String formatDatabaseType(String databaseType) {
        String[] databaseTypeParts = databaseType.split("-");
        return StringUtils.capitalize(databaseTypeParts[0]) + StringUtils.capitalize(databaseTypeParts[1]);
    }

    public static String formatTableParameter(String param, String mode) throws Exception {
        if (mode.equals("short")) {
            if (param.length() > 15) {
                return StringUtils.capitalize(param.substring(0, 11));
            }
            return StringUtils.capitalize(param);
        } else {
            throw new Exception(String.format("Incorrect mode parameter: %s", mode));
        }
    }

    public static List<String[]> getCategoryToSubcategory() {
        return List.of(clothesNames, accessoriesNames);
    }  
}
