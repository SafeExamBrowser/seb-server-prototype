INSERT INTO institution VALUES
    (1, 'ETH Zürich', 'INTERNAL')
    ;
    
INSERT INTO seb_lms_setup VALUES
    (1, 1, 'Setup 1', 'MOCKUP', 'https://localhost:8090/lms/authorisationRequest', 'lmsclient', '$2a$04$KtWx03i61fPqibYXyzd/6.ZKsmxJuPlQgFKaDnZLmHbnSuhjbmi3a', null, 'sebclient', '$2a$04$SkWmoq0fL2wWyEk.cQj98ewdBH0Jtj.JWH/2vo6fJpo8wgK2/iv1W'),
    (2, 1, 'Setup 2', 'MOCKUP', null, 'lmsclient2', '$2a$04$KtWx03i61fPqibYXyzd/6.ZKsmxJuPlQgFKaDnZLmHbnSuhjbmi3a', null, 'sebclient2', '$2a$04$GnvmGGhTz8O3XA6w0Rrwk.gDsdgLmwOfApmxmoVmWkl3bZ.oP0.ry')
    ;

INSERT INTO user VALUES 
    (1, 1, 'internalDemoAdmin', 'Admin1', 'admin', '$2a$08$c2GKYEYoUVXH1Yb8GXVXVu66ltPvbZgLMcVSXRH.LgZNF/YeaYB8m', 'admin@nomail.nomail', '2018-01-01 00:00:00', 1, 'en', 'UTC'),
    (2, 1, 'internalUser1', 'User1', 'user1', '$2a$08$0mBB2.NPUsg0afMhYq5NE.Te.g.QnzIH8ncKs65zYtoyV/NV2X8ne', 'user1@nomail.nomail', '2018-01-01 00:00:00', 1, 'de', 'UTC'),
    (3, 1, 'internalUser2', 'User2', 'user2', '$2a$08$VSQl35ERwunAEKMGt7XhkuqkDD4ze4I8j/RDdRw8jzqhjwzuqiGQi', 'user2@nomail.nomail', '2018-01-01 00:00:00', 1, 'en', 'UTC')
    ;
    
INSERT INTO user_role VALUES
    (1, 1, 'SEB_SERVER_ADMIN'),
    (2, 2, 'EXAM_SUPPORTER'),
    (3, 3, 'EXAM_SUPPORTER'),
    (4, 1, 'EXAM_SUPPORTER')
    ;

INSERT INTO configuration_attribute VALUES
    (1, 'table1', 'TABLE', null, '', '', '', ''),
    (2, 'param1', 'TEXT_FIELD', 1, '', '', '', ''),
    (3, 'param2', 'SINGLE_SELECTION', 1, '1,2,3', '', '', ''),
    (4, 'param3', 'TEXT_FIELD', 1, '', '', '', ''),
    
    (5, 'check1', 'CHECKBOX', null, '', '', '', ''),
    (6, 'check2', 'CHECKBOX', null, '', '', '', ''),
    
    (7, 'Label1:', 'LABEL', null, '', '', '', ''),
    (8, 'check11', 'CHECKBOX', null, '', '', '', ''),
    (9, 'check12', 'CHECKBOX', null, '', '', '', ''),
    
    (10, 'check21', 'CHECKBOX', null, '', '', '', ''),
    (11, 'check22', 'CHECKBOX', null, '', '', '', ''),
    (12, 'check23', 'CHECKBOX', null, '', '', '', ''),
    
    (13, 'text1', 'TEXT_FIELD', null, '', '', '', ''),
    (14, 'number1', 'INTEGER', null, '', '', '', ''),
    (15, 'checkField1', 'CHECK_FIELD', null, '', '', '', ''),
    (16, 'checkField2', 'CHECK_FIELD', null, '', '', '', ''),
    (17, 'selection1', 'SINGLE_SELECTION', null, 'One,Two,Three', '', '', '')
    ;
    
INSERT INTO orientation VALUES 
    (1, 1, 'view1', null, 0, 13, 4, 4, null),
    (2, 2, 'view1', null, 0, 0, 1, 1, null),
    (3, 3, 'view1', null, 1, 0, 1, 1, null),
    (4, 4, 'view1', null, 2, 0, 1, 1, null),
    
    (5, 5, 'view1', null, 0, 0, 1, 1, null),
    (6, 6, 'view1', null, 0, 1, 1, 1, null),
    
    (7, 7, 'view1', null, 1, 0, 1, 1, null),
    (8, 8, 'view1', null, 1, 1, 1, 1, null),
    (9, 9, 'view1', null, 1, 2, 1, 1, null),
    
    (10, 10, 'view1', 'Group1', 2, 0, 1, 1, null),
    (11, 11, 'view1', 'Group1', 2, 1, 1, 1, null),
    (12, 12, 'view1', 'Group1', 2, 2, 1, 1, null),
    
    (13, 13, 'view1', null, 1, 6, 1, 1, null),
    (14, 14, 'view1', null, 1, 7, 1, 1, null),
    (15, 15, 'view1', null, 1, 8, 1, 1, null),
    (16, 16, 'view1', null, 1, 9, 1, 1, null),
    (17, 17, 'view1', null, 1, 10, 1, 1, null)
    ;
    
INSERT INTO configuration_node VALUES
    (1, 1, 3, 'Demo Config', 'Demo Config', 'CLIENT', null)
    ;
    
INSERT INTO configuration VALUES
    (1, 1, 'V1.0', '2018-01-01 00:00:00', 1)
    ;
    
INSERT INTO `exam` VALUES 
    (1, 1, 'Demo Exam 1', 'internalUser1', null, 'MANAGED'),
    (2, 1, 'Demo Exam 2', 'internalUser1', null, 'BYOD'),
    (3, 1, 'Demo Exam 3', 'internalUser2', null, 'MANAGED'),
    (4, 1, 'Demo Exam 4', 'internalUser2', null, 'BYOD')
    ;

INSERT INTO indicator VALUES 
    (1,1,'DemoIndicator1', 'Demo Indicator', 20.0000,40.0000,60.0000),
    (2,1,'errorCountIndicator', 'Incident Count', 1.0000,2.0000,3.0000),
    (3,3,'errorCountIndicator', 'Incident Count', 1.0000,2.0000,3.0000),
    (4,4,'errorCountIndicator', 'Incident Count', 1.0000,2.0000,3.0000),
    (5,3,'pingIntervalIndicator', 'Ping', 1000,2000,5000),
    (6,4,'pingIntervalIndicator', 'Ping', 1000,2000,5000)
    ;

