CREATE TABLE `ligue` (
    `ID_Ligue` int NOT NULL AUTO_INCREMENT,
    `Nom_Ligue` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`ID_Ligue`)
);

CREATE TABLE `employe` (
    `ID_Employe` int NOT NULL AUTO_INCREMENT,
    `Nom_Employe` varchar(20) DEFAULT NULL,
    `Prenom_Employe` varchar(20) DEFAULT NULL,
    `Mail_Employe` varchar(50) DEFAULT NULL,
    `MDP_Employe` varchar(50) DEFAULT NULL,
    `Fonction_Employe` varchar(50) DEFAULT NULL,
    `Date_Depart` date DEFAULT NULL,
    `Date_Arrivee` date DEFAULT NULL,
    `ID_Ligue` int DEFAULT NULL,
    PRIMARY KEY (`ID_Employe`),
    KEY `fk_ligue` (`ID_Ligue`),
    CONSTRAINT `fk_ligue` FOREIGN KEY (`ID_Ligue`) REFERENCES `ligue` (`ID_Ligue`) ON DELETE CASCADE
);