INSERT INTO `orientation` VALUES
    (1, "", 0, 0);
    
INSERT INTO `configuration_attribute` values
    (1, "additionalDictionaries", "COMPLEX_ATTRIBUTE", null, 1),
    (2, "dictionaryData", "BASE64_BINARY", 1, 1),
    (3, "dictionaryFormat", "INTEGER", 1, 1),
    (4, "localeName", "STRING", 1, 1);

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

