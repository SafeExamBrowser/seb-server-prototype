INSERT INTO `configuration_attribute` values
    (1, "table1", "COMPLEX_ATTRIBUTE", null, "", "", ""),
    (2, "table1Param1", "BASE64_BINARY", 1, "", "", ""),
    (3, "table1Param2", "SINGLE_SELECTION", 1, "One,Two,Tree", "", ""),
    (4, "table1Param3", "TEXT_FIELD", 1, "", "", ""),
    (5, "check1", "CHECKBOX", null, "", "", ""),
    (6, "check2", "CHECKBOX", null, "", "", "")
    ;
    
INSERT INTO `orientation` values 
    (1, 1, "view1", "table1", 0, 3),
    (2, 2, "view1", "table1", 0, 0),
    (3, 3, "view1", "table1", 1, 0),
    (4, 4, "view1", "table1", 2, 0),
    (5, 5, "view1", "checkA", 0, 0),
    (6, 6, "view1", "checkA", 0, 1);

INSERT INTO `exam` VALUES 
    (1,'Demo Exam 1',0),
    (2,'Demo Exam 2',0),
    (3,'Demo Exam 3',1),
    (4,'Demo Exam 4',2);
    
INSERT INTO `indicator` VALUES 
    (NULL,1,"DemoIndicator1",20.0000,40.0000,60.0000),
    (NULL,1,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,3,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,4,"errorCountIndicator",1.0000,2.0000,3.0000),
    (NULL,3,"pingIntervalIndicator",1000,2000,5000),
    (NULL,4,"pingIntervalIndicator",1000,2000,5000);

