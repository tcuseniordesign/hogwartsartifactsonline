INSERT INTO wizard (name) VALUES ('Albus Dumbledore');
INSERT INTO wizard (name) VALUES ('Harry Potter');
INSERT INTO wizard (name) VALUES ('Neville Longbottom');

INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904191', 'Deluminator', 'A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.', 'imageUrl', 1);
INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904192', 'Invisibility Cloak', 'An invisibility cloak is used to make the wearer invisible.', 'imageUrl', 2);
INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904193', 'Elder Wand', 'The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.', 'imageUrl', 1);
INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904194', 'The Marauder''s Map', 'A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.', 'imageUrl', 2);
INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904195', 'The Sword Of Gryffindor', 'A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.', 'imageUrl', 3);
INSERT INTO artifact (id, name, description, image_url, owner_id) VALUES ('1250808601744904196', 'Resurrection Stone', 'The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.', 'imageUrl', null);

INSERT INTO hogwarts_user (username, password, enabled, roles) VALUES ('john', '$2a$12$f.xloZgyVVOW3Mezj0ES..76BJC8ThAJO/otgT2haaP4IPrpyPphW', true, 'admin user');
INSERT INTO hogwarts_user (username, password, enabled, roles) VALUES ('eric', '$2a$12$cajJAR6uzi8Wyo2mj4o5c.Z3XYdJkHLP9aVPCRP5Xgh3wNNksnxsG', true, 'user');
INSERT INTO hogwarts_user (username, password, enabled, roles) VALUES ('tom', '$2a$12$f.xloZgyVVOW3Mezj0ES..76BJC8ThAJO/otgT2haaP4IPrpyPphW', false, 'user');

