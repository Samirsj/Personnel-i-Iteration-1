
CREATE TABLE `Ligue` (
    `ID_Ligue` int NOT NULL AUTO_INCREMENT,
    `Nom_Ligue` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`ID_Ligue`)
);


CREATE TABLE `Employe` (
    `ID_Employé` int NOT NULL AUTO_INCREMENT,
    `Nom_Employé` varchar(20) DEFAULT NULL,
    `Prenom_Employé` varchar(20) DEFAULT NULL,
    `Mail_Employé` varchar(50) DEFAULT NULL,
    `MDP_Employé` varchar(50) DEFAULT NULL,
    `Fonction_Employé` varchar(50) DEFAULT NULL,
    `Date_Depart` date DEFAULT NULL,
    `Date_Arrivee` date DEFAULT NULL,
    `ID_Ligue` int DEFAULT NULL,
    PRIMARY KEY (`ID_Employé`),
    KEY `fk_ligue` (`ID_Ligue`),
    CONSTRAINT `fk_ligue` FOREIGN KEY (`ID_Ligue`) REFERENCES `ligue` (`ID_Ligue`) ON DELETE CASCADE
);
