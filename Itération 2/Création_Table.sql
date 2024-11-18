

CREATE TABLE Ligue (
    ID_Ligue INT ,
    Nom_Ligue VARCHAR(50),
    PRIMARY KEY(ID_Ligue)
);


CREATE TABLE Employé (
    ID_Employé INT ,
    Nom_Employé VARCHAR(20),
    Prenom_Employé VARCHAR(20),
    Mail_Employé VARCHAR(50),
    MDP_Employé VARCHAR(50),
    Fonction_Employé VARCHAR(50),
    Date_Depart DATE,
    Date_Arrivee DATE,
    ID_Ligue INT,
    PRIMARY KEY(ID_Employé),
    FOREIGN KEY(ID_Ligue) REFERENCES Ligue(ID_Ligue)
);