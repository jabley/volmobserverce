DROP TABLE IF EXISTS JSTORE;
DROP TABLE IF EXISTS JOFFERS;

CREATE TABLE JSTORE (
  PRODUCT VARCHAR (50) NOT NULL,
  DESCRIPTION VARCHAR (255) NOT NULL,
  PRICE CHAR (10) NOT NULL );
  
CREATE TABLE JOFFERS (
  PRODUCT VARCHAR (50) NOT NULL,
  DESCRIPTION VARCHAR (255) NOT NULL,
  PRICE CHAR (10) NOT NULL );
  
INSERT INTO JSTORE ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Storm Jacket', 'Jive-Tek Nylon shell Storm Jacket is water repellent, wind resistant, has a front zipper and elastic waistband. ', '$19.99');
INSERT INTO JSTORE ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Long Sleeve Sport Shirt', 'Cotton, long-sleeve pique knit sport shirt. Designed with a full cut and soft, comfortable look to please anyone. ', '$15.98' );
INSERT INTO JSTORE ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Short Sleeve Twill Shirt', 'This classic 100% cotton, twill short sleeve shirt has a patch pocket and a button-down collar. ', '$15.98' );
INSERT INTO JSTORE ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Jersey Knit Sport Shirt', 'This cotton jersey knit sport shirt is unique and upscale, perfect for the office or out on the putting green. ', '$14.98' );
INSERT INTO JSTORE ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Cotton Sport Shirt', 'This 100% cotton shirt pays has horn tone buttons, welt collar and cuffs, side vents, and locker patch. ', '$11.98' );

INSERT INTO JOFFERS ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Long Sleeve Twill Shirt', 'This classic 100% cotton, twill long sleeve shirt has a patch pocket and a button-down collar. ', '$15.98' );
INSERT INTO JOFFERS ( PRODUCT, DESCRIPTION, PRICE ) VALUES ( 'Short Sleeve Denim Shirt', 'This classic cotton, 6.5-ounce stonewashed denim shirt is perfect for a corporate or casual situation. ', '$15.98' );
