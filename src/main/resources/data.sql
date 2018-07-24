INSERT INTO user VALUES 
    (1, "Admin1", "admin", "$2a$08$c2GKYEYoUVXH1Yb8GXVXVu66ltPvbZgLMcVSXRH.LgZNF/YeaYB8m", "admin@nomail.nomail", '2018-01-01 00:00:00', 1),
    (2, "User1", "user1", "$2a$08$XkDcqOPfp6L6CkRlYUZ61e1pT.ut8NblPE4IujHIiOzxQkVwo2O8q", "user1@nomail.nomail", '2018-01-01 00:00:00', 1)
    ;
    
INSERT INTO user_role VALUES
    (1, 1, "ADMIN_USER"),
    (2, 2, "STANDARD_USER")
    ;

INSERT INTO configuration_attribute VALUES
    (1, "table1", "TABLE", null, "", "", ""),
    (2, "param1", "TEXT_FIELD", 1, "", "", ""),
    (3, "param2", "SINGLE_SELECTION", 1, "One,Two,Three", "", ""),
    (4, "param3", "TEXT_FIELD", 1, "", "", ""),
    
    (5, "check1", "CHECKBOX", null, "", "", ""),
    (6, "check2", "CHECKBOX", null, "", "", ""),
    
    (7, "Label1:", "LABEL", null, "", "", ""),
    (8, "check11", "CHECKBOX", null, "", "", ""),
    (9, "check12", "CHECKBOX", null, "", "", ""),
    
    (10, "check21", "CHECKBOX", null, "", "", ""),
    (11, "check22", "CHECKBOX", null, "", "", ""),
    (12, "check23", "CHECKBOX", null, "", "", ""),
    
    (13, "text1", "TEXT_FIELD", null, "", "", ""),
    (14, "number1", "INTEGER", null, "", "", ""),
    (15, "checkField1", "CHECK_FIELD", null, "", "", ""),
    (16, "checkField2", "CHECK_FIELD", null, "", "", ""),
    (17, "selection1", "SINGLE_SELECTION", null, "One,Two,Three", "", "")
    ;
    
INSERT INTO orientation VALUES 
    (1, 1, "view1", null, 0, 13),
    (2, 2, "view1", null, 0, 0),
    (3, 3, "view1", null, 1, 0),
    (4, 4, "view1", null, 2, 0),
    
    (5, 5, "view1", null, 0, 0),
    (6, 6, "view1", null, 0, 1),
    
    (7, 7, "view1", null, 1, 0),
    (8, 8, "view1", null, 1, 1),
    (9, 9, "view1", null, 1, 2),
    
    (10, 10, "view1", "Group1", 2, 0),
    (11, 11, "view1", "Group1", 2, 1),
    (12, 12, "view1", "Group1", 2, 2),
    
    (13, 13, "view1", null, 1, 6),
    (14, 14, "view1", null, 1, 7),
    (15, 15, "view1", null, 1, 8),
    (16, 16, "view1", null, 1, 9),
    (17, 17, "view1", null, 1, 10)
    ;
    
INSERT INTO configuration VALUES
    (1, "Demo Config", "CLIENT", 2);

INSERT INTO `exam` VALUES 
    (1,'Demo Exam 1',0, 2),
    (2,'Demo Exam 2',0, 2),
    (3,'Demo Exam 3',1, 2),
    (4,'Demo Exam 4',2, 2);
    
INSERT INTO indicator VALUES 
    (NULL,1,"DemoIndicator1",20.0000,40.0000,60.0000),
    (NULL,1,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,3,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,4,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,3,"pingIntervalIndicator",1000,2000,5000),
    (NULL,4,"pingIntervalIndicator",1000,2000,5000);

