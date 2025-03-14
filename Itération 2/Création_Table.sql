
CREATE TABLE `Ligue` (
    `ID_Ligue` int NOT NULL AUTO_INCREMENT,
    `Nom_Ligue` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`ID_Ligue`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `Employe` (
    `ID_Employé` int NOT NULL AUTO_INCREMENT,
    `Nom_Employé` varchar(20) DEFAULT NULL,
    `Prenom_Employé` varchar(20) DEFAULT NULL,
    `Mail_Employé` varchar(50) DEFAULT NULL,
    `MDP_Employé` varchar(50) DEFAULT NULL,
    `Fonction_Employe` varchar(50) DEFAULT NULL,
    `Date_Depart` date DEFAULT NULL,
    `Date_Arrivee` date DEFAULT NULL,
    `ID_Ligue` int DEFAULT NULL,
    PRIMARY KEY (`ID_Employe`),
    KEY `fk_ligue` (`ID_Ligue`),
    CONSTRAINT `fk_ligue` FOREIGN KEY (`ID_Ligue`) REFERENCES `ligue` (`ID_Ligue`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
