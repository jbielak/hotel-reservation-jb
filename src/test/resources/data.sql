CREATE TABLE IF NOT  EXISTS room (
  id          INTEGER PRIMARY KEY auto_increment,
  room_type    VARCHAR NOT NULL,
  PRIMARY KEY (id));

INSERT INTO room VALUES(1, 'PENTHOUSE');
INSERT INTO room VALUES(2, 'BASIC');

DROP TABLE reservation;

CREATE TABLE IF NOT EXISTS reservation (
  id          INTEGER PRIMARY KEY auto_increment,
  username    VARCHAR(64) NOT NULL,
  number_of_people INTEGER NOT NULL,
  start_date  DATE NOT NULL,
  end_date    DATE NOT NULL,
  room_id INTEGER,
  PRIMARY KEY (id),
  FOREIGN KEY (room_id) REFERENCES room (id));

INSERT INTO reservation VALUES(1, 'test_user', 7, '2019-12-14', '2019-12-20', 1);
INSERT INTO reservation VALUES(2, 'test_user', 8, '2019-12-22', '2019-12-28', 1);
INSERT INTO reservation VALUES(3, 'test_user', 3, '2019-12-12', '2019-12-28', 2);