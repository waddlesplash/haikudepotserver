CREATE TABLE haikudepot.country (
  id BIGINT NOT NULL,
  code VARCHAR(2) NOT NULL,
  name VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE haikudepot.repository_source_mirror (
  id BIGINT NOT NULL,
  repository_source_id BIGINT NOT NULL,
  country_id BIGINT NOT NULL,
  base_url VARCHAR(1024) NOT NULL,
  description VARCHAR(4096),
  active BOOLEAN NOT NULL,
  is_primary BOOLEAN NOT NULL,
  create_timestamp TIMESTAMP NOT NULL,
  modify_timestamp TIMESTAMP NOT NULL,
  code VARCHAR(36) NOT NULL,
  PRIMARY KEY (id),
  -- makes sure that there can be a maximum of one primary mirror per
  -- repository source.
  EXCLUDE (is_primary WITH =, repository_source_id WITH =) WHERE (is_primary)
    DEFERRABLE INITIALLY DEFERRED
);

CREATE SEQUENCE haikudepot.country_seq
  START WITH 9312 INCREMENT BY 1;

CREATE SEQUENCE haikudepot.repository_source_mirror_seq
  START WITH 4121 INCREMENT BY 1;

CREATE UNIQUE INDEX country_idx01 ON haikudepot.country(code);

CREATE UNIQUE INDEX repository_source_mirror_idx02 ON
  haikudepot.repository_source_mirror(base_url, repository_source_id);

ALTER TABLE haikudepot.repository_source_mirror
  ADD FOREIGN KEY (country_id)
  REFERENCES haikudepot.country (id);

ALTER TABLE haikudepot.repository_source_mirror
  ADD FOREIGN KEY (repository_source_id)
  REFERENCES haikudepot.repository_source (id);

-- these insert statements to initially populate this table have been taken from
-- https://pkgstore.datahub.io/core/country-list/data_csv/data/d7c9d7cfb42cb69f4422dec222dbbaa8/data_csv.csv
-- (2018-07-19)

INSERT INTO haikudepot.country (id, code, name, active) VALUES
(NEXTVAL('haikudepot.country_seq'),'AF','Afghanistan',true),
(NEXTVAL('haikudepot.country_seq'),'AX','Åland Islands',true),
(NEXTVAL('haikudepot.country_seq'),'AL','Albania',true),
(NEXTVAL('haikudepot.country_seq'),'DZ','Algeria',true),
(NEXTVAL('haikudepot.country_seq'),'AS','American Samoa',true),
(NEXTVAL('haikudepot.country_seq'),'AD','Andorra',true),
(NEXTVAL('haikudepot.country_seq'),'AO','Angola',true),
(NEXTVAL('haikudepot.country_seq'),'AI','Anguilla',true),
(NEXTVAL('haikudepot.country_seq'),'AQ','Antarctica',true),
(NEXTVAL('haikudepot.country_seq'),'AG','Antigua and Barbuda',true),
(NEXTVAL('haikudepot.country_seq'),'AR','Argentina',true),
(NEXTVAL('haikudepot.country_seq'),'AM','Armenia',true),
(NEXTVAL('haikudepot.country_seq'),'AW','Aruba',true),
(NEXTVAL('haikudepot.country_seq'),'AU','Australia',true),
(NEXTVAL('haikudepot.country_seq'),'AT','Austria',true),
(NEXTVAL('haikudepot.country_seq'),'AZ','Azerbaijan',true),
(NEXTVAL('haikudepot.country_seq'),'BS','Bahamas',true),
(NEXTVAL('haikudepot.country_seq'),'BH','Bahrain',true),
(NEXTVAL('haikudepot.country_seq'),'BD','Bangladesh',true),
(NEXTVAL('haikudepot.country_seq'),'BB','Barbados',true),
(NEXTVAL('haikudepot.country_seq'),'BY','Belarus',true),
(NEXTVAL('haikudepot.country_seq'),'BE','Belgium',true),
(NEXTVAL('haikudepot.country_seq'),'BZ','Belize',true),
(NEXTVAL('haikudepot.country_seq'),'BJ','Benin',true),
(NEXTVAL('haikudepot.country_seq'),'BM','Bermuda',true),
(NEXTVAL('haikudepot.country_seq'),'BT','Bhutan',true),
(NEXTVAL('haikudepot.country_seq'),'BO','Bolivia, Plurinational State of',true),
(NEXTVAL('haikudepot.country_seq'),'BQ','Bonaire, Sint Eustatius and Saba',true),
(NEXTVAL('haikudepot.country_seq'),'BA','Bosnia and Herzegovina',true),
(NEXTVAL('haikudepot.country_seq'),'BW','Botswana',true),
(NEXTVAL('haikudepot.country_seq'),'BV','Bouvet Island',true),
(NEXTVAL('haikudepot.country_seq'),'BR','Brazil',true),
(NEXTVAL('haikudepot.country_seq'),'IO','British Indian Ocean Territory',true),
(NEXTVAL('haikudepot.country_seq'),'BN','Brunei Darussalam',true),
(NEXTVAL('haikudepot.country_seq'),'BG','Bulgaria',true),
(NEXTVAL('haikudepot.country_seq'),'BF','Burkina Faso',true),
(NEXTVAL('haikudepot.country_seq'),'BI','Burundi',true),
(NEXTVAL('haikudepot.country_seq'),'KH','Cambodia',true),
(NEXTVAL('haikudepot.country_seq'),'CM','Cameroon',true),
(NEXTVAL('haikudepot.country_seq'),'CA','Canada',true),
(NEXTVAL('haikudepot.country_seq'),'CV','Cape Verde',true),
(NEXTVAL('haikudepot.country_seq'),'KY','Cayman Islands',true),
(NEXTVAL('haikudepot.country_seq'),'CF','Central African Republic',true),
(NEXTVAL('haikudepot.country_seq'),'TD','Chad',true),
(NEXTVAL('haikudepot.country_seq'),'CL','Chile',true),
(NEXTVAL('haikudepot.country_seq'),'CN','China',true),
(NEXTVAL('haikudepot.country_seq'),'CX','Christmas Island',true),
(NEXTVAL('haikudepot.country_seq'),'CC','Cocos (Keeling) Islands',true),
(NEXTVAL('haikudepot.country_seq'),'CO','Colombia',true),
(NEXTVAL('haikudepot.country_seq'),'KM','Comoros',true),
(NEXTVAL('haikudepot.country_seq'),'CG','Congo',true),
(NEXTVAL('haikudepot.country_seq'),'CD','Congo, the Democratic Republic of the',true),
(NEXTVAL('haikudepot.country_seq'),'CK','Cook Islands',true),
(NEXTVAL('haikudepot.country_seq'),'CR','Costa Rica',true),
(NEXTVAL('haikudepot.country_seq'),'CI','Côte d''Ivoire',true),
(NEXTVAL('haikudepot.country_seq'),'HR','Croatia',true),
(NEXTVAL('haikudepot.country_seq'),'CU','Cuba',true),
(NEXTVAL('haikudepot.country_seq'),'CW','Curaçao',true),
(NEXTVAL('haikudepot.country_seq'),'CY','Cyprus',true),
(NEXTVAL('haikudepot.country_seq'),'CZ','Czech Republic',true),
(NEXTVAL('haikudepot.country_seq'),'DK','Denmark',true),
(NEXTVAL('haikudepot.country_seq'),'DJ','Djibouti',true),
(NEXTVAL('haikudepot.country_seq'),'DM','Dominica',true),
(NEXTVAL('haikudepot.country_seq'),'DO','Dominican Republic',true),
(NEXTVAL('haikudepot.country_seq'),'EC','Ecuador',true),
(NEXTVAL('haikudepot.country_seq'),'EG','Egypt',true),
(NEXTVAL('haikudepot.country_seq'),'SV','El Salvador',true),
(NEXTVAL('haikudepot.country_seq'),'GQ','Equatorial Guinea',true),
(NEXTVAL('haikudepot.country_seq'),'ER','Eritrea',true),
(NEXTVAL('haikudepot.country_seq'),'EE','Estonia',true),
(NEXTVAL('haikudepot.country_seq'),'ET','Ethiopia',true),
(NEXTVAL('haikudepot.country_seq'),'FK','Falkland Islands (Malvinas)',true),
(NEXTVAL('haikudepot.country_seq'),'FO','Faroe Islands',true),
(NEXTVAL('haikudepot.country_seq'),'FJ','Fiji',true),
(NEXTVAL('haikudepot.country_seq'),'FI','Finland',true),
(NEXTVAL('haikudepot.country_seq'),'FR','France',true),
(NEXTVAL('haikudepot.country_seq'),'GF','French Guiana',true),
(NEXTVAL('haikudepot.country_seq'),'PF','French Polynesia',true),
(NEXTVAL('haikudepot.country_seq'),'TF','French Southern Territories',true),
(NEXTVAL('haikudepot.country_seq'),'GA','Gabon',true),
(NEXTVAL('haikudepot.country_seq'),'GM','Gambia',true),
(NEXTVAL('haikudepot.country_seq'),'GE','Georgia',true),
(NEXTVAL('haikudepot.country_seq'),'DE','Germany',true), -- eu.hpkg.haiku-os.org
(NEXTVAL('haikudepot.country_seq'),'GH','Ghana',true),
(NEXTVAL('haikudepot.country_seq'),'GI','Gibraltar',true),
(NEXTVAL('haikudepot.country_seq'),'GR','Greece',true),
(NEXTVAL('haikudepot.country_seq'),'GL','Greenland',true),
(NEXTVAL('haikudepot.country_seq'),'GD','Grenada',true),
(NEXTVAL('haikudepot.country_seq'),'GP','Guadeloupe',true),
(NEXTVAL('haikudepot.country_seq'),'GU','Guam',true),
(NEXTVAL('haikudepot.country_seq'),'GT','Guatemala',true),
(NEXTVAL('haikudepot.country_seq'),'GG','Guernsey',true),
(NEXTVAL('haikudepot.country_seq'),'GN','Guinea',true),
(NEXTVAL('haikudepot.country_seq'),'GW','Guinea-Bissau',true),
(NEXTVAL('haikudepot.country_seq'),'GY','Guyana',true),
(NEXTVAL('haikudepot.country_seq'),'HT','Haiti',true),
(NEXTVAL('haikudepot.country_seq'),'HM','Heard Island and McDonald Islands',true),
(NEXTVAL('haikudepot.country_seq'),'VA','Holy See (Vatican City State)',true),
(NEXTVAL('haikudepot.country_seq'),'HN','Honduras',true),
(NEXTVAL('haikudepot.country_seq'),'HK','Hong Kong',true),
(NEXTVAL('haikudepot.country_seq'),'HU','Hungary',true),
(NEXTVAL('haikudepot.country_seq'),'IS','Iceland',true),
(NEXTVAL('haikudepot.country_seq'),'IN','India',true),
(NEXTVAL('haikudepot.country_seq'),'ID','Indonesia',true),
(NEXTVAL('haikudepot.country_seq'),'IR','Iran, Islamic Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'IQ','Iraq',true),
(NEXTVAL('haikudepot.country_seq'),'IE','Ireland',true),
(NEXTVAL('haikudepot.country_seq'),'IM','Isle of Man',true),
(NEXTVAL('haikudepot.country_seq'),'IL','Israel',true),
(NEXTVAL('haikudepot.country_seq'),'IT','Italy',true),
(NEXTVAL('haikudepot.country_seq'),'JM','Jamaica',true),
(NEXTVAL('haikudepot.country_seq'),'JP','Japan',true),
(NEXTVAL('haikudepot.country_seq'),'JE','Jersey',true),
(NEXTVAL('haikudepot.country_seq'),'JO','Jordan',true),
(NEXTVAL('haikudepot.country_seq'),'KZ','Kazakhstan',true),
(NEXTVAL('haikudepot.country_seq'),'KE','Kenya',true),
(NEXTVAL('haikudepot.country_seq'),'KI','Kiribati',true),
(NEXTVAL('haikudepot.country_seq'),'KP','Korea, Democratic People''s Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'KR','Korea, Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'KW','Kuwait',true),
(NEXTVAL('haikudepot.country_seq'),'KG','Kyrgyzstan',true),
(NEXTVAL('haikudepot.country_seq'),'LA','Lao People''s Democratic Republic',true),
(NEXTVAL('haikudepot.country_seq'),'LV','Latvia',true),
(NEXTVAL('haikudepot.country_seq'),'LB','Lebanon',true),
(NEXTVAL('haikudepot.country_seq'),'LS','Lesotho',true),
(NEXTVAL('haikudepot.country_seq'),'LR','Liberia',true),
(NEXTVAL('haikudepot.country_seq'),'LY','Libya',true),
(NEXTVAL('haikudepot.country_seq'),'LI','Liechtenstein',true),
(NEXTVAL('haikudepot.country_seq'),'LT','Lithuania',true),
(NEXTVAL('haikudepot.country_seq'),'LU','Luxembourg',true),
(NEXTVAL('haikudepot.country_seq'),'MO','Macao',true),
(NEXTVAL('haikudepot.country_seq'),'MK','Macedonia, the Former Yugoslav Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'MG','Madagascar',true),
(NEXTVAL('haikudepot.country_seq'),'MW','Malawi',true),
(NEXTVAL('haikudepot.country_seq'),'MY','Malaysia',true),
(NEXTVAL('haikudepot.country_seq'),'MV','Maldives',true),
(NEXTVAL('haikudepot.country_seq'),'ML','Mali',true),
(NEXTVAL('haikudepot.country_seq'),'MT','Malta',true),
(NEXTVAL('haikudepot.country_seq'),'MH','Marshall Islands',true),
(NEXTVAL('haikudepot.country_seq'),'MQ','Martinique',true),
(NEXTVAL('haikudepot.country_seq'),'MR','Mauritania',true),
(NEXTVAL('haikudepot.country_seq'),'MU','Mauritius',true),
(NEXTVAL('haikudepot.country_seq'),'YT','Mayotte',true),
(NEXTVAL('haikudepot.country_seq'),'MX','Mexico',true),
(NEXTVAL('haikudepot.country_seq'),'FM','Micronesia, Federated States of',true),
(NEXTVAL('haikudepot.country_seq'),'MD','Moldova, Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'MC','Monaco',true),
(NEXTVAL('haikudepot.country_seq'),'MN','Mongolia',true),
(NEXTVAL('haikudepot.country_seq'),'ME','Montenegro',true),
(NEXTVAL('haikudepot.country_seq'),'MS','Montserrat',true),
(NEXTVAL('haikudepot.country_seq'),'MA','Morocco',true),
(NEXTVAL('haikudepot.country_seq'),'MZ','Mozambique',true),
(NEXTVAL('haikudepot.country_seq'),'MM','Myanmar',true),
(NEXTVAL('haikudepot.country_seq'),'NA','Namibia',true),
(NEXTVAL('haikudepot.country_seq'),'NR','Nauru',true),
(NEXTVAL('haikudepot.country_seq'),'NP','Nepal',true),
(NEXTVAL('haikudepot.country_seq'),'NL','Netherlands',true),
(NEXTVAL('haikudepot.country_seq'),'NC','New Caledonia',true),
(NEXTVAL('haikudepot.country_seq'),'NZ','New Zealand',true),
(NEXTVAL('haikudepot.country_seq'),'NI','Nicaragua',true),
(NEXTVAL('haikudepot.country_seq'),'NE','Niger',true),
(NEXTVAL('haikudepot.country_seq'),'NG','Nigeria',true),
(NEXTVAL('haikudepot.country_seq'),'NU','Niue',true),
(NEXTVAL('haikudepot.country_seq'),'NF','Norfolk Island',true),
(NEXTVAL('haikudepot.country_seq'),'MP','Northern Mariana Islands',true),
(NEXTVAL('haikudepot.country_seq'),'NO','Norway',true),
(NEXTVAL('haikudepot.country_seq'),'OM','Oman',true),
(NEXTVAL('haikudepot.country_seq'),'PK','Pakistan',true),
(NEXTVAL('haikudepot.country_seq'),'PW','Palau',true),
(NEXTVAL('haikudepot.country_seq'),'PS','Palestine, State of',true),
(NEXTVAL('haikudepot.country_seq'),'PA','Panama',true),
(NEXTVAL('haikudepot.country_seq'),'PG','Papua New Guinea',true),
(NEXTVAL('haikudepot.country_seq'),'PY','Paraguay',true),
(NEXTVAL('haikudepot.country_seq'),'PE','Peru',true),
(NEXTVAL('haikudepot.country_seq'),'PH','Philippines',true),
(NEXTVAL('haikudepot.country_seq'),'PN','Pitcairn',true),
(NEXTVAL('haikudepot.country_seq'),'PL','Poland',true),
(NEXTVAL('haikudepot.country_seq'),'PT','Portugal',true),
(NEXTVAL('haikudepot.country_seq'),'PR','Puerto Rico',true),
(NEXTVAL('haikudepot.country_seq'),'QA','Qatar',true),
(NEXTVAL('haikudepot.country_seq'),'RE','Réunion',true),
(NEXTVAL('haikudepot.country_seq'),'RO','Romania',true),
(NEXTVAL('haikudepot.country_seq'),'RU','Russian Federation',true),
(NEXTVAL('haikudepot.country_seq'),'RW','Rwanda',true),
(NEXTVAL('haikudepot.country_seq'),'BL','Saint Barthélemy',true),
(NEXTVAL('haikudepot.country_seq'),'SH','Saint Helena, Ascension and Tristan da Cunha',true),
(NEXTVAL('haikudepot.country_seq'),'KN','Saint Kitts and Nevis',true),
(NEXTVAL('haikudepot.country_seq'),'LC','Saint Lucia',true),
(NEXTVAL('haikudepot.country_seq'),'MF','Saint Martin (French part)',true),
(NEXTVAL('haikudepot.country_seq'),'PM','Saint Pierre and Miquelon',true),
(NEXTVAL('haikudepot.country_seq'),'VC','Saint Vincent and the Grenadines',true),
(NEXTVAL('haikudepot.country_seq'),'WS','Samoa',true),
(NEXTVAL('haikudepot.country_seq'),'SM','San Marino',true),
(NEXTVAL('haikudepot.country_seq'),'ST','Sao Tome and Principe',true),
(NEXTVAL('haikudepot.country_seq'),'SA','Saudi Arabia',true),
(NEXTVAL('haikudepot.country_seq'),'SN','Senegal',true),
(NEXTVAL('haikudepot.country_seq'),'RS','Serbia',true),
(NEXTVAL('haikudepot.country_seq'),'SC','Seychelles',true),
(NEXTVAL('haikudepot.country_seq'),'SL','Sierra Leone',true),
(NEXTVAL('haikudepot.country_seq'),'SG','Singapore',true),
(NEXTVAL('haikudepot.country_seq'),'SX','Sint Maarten (Dutch part)',true),
(NEXTVAL('haikudepot.country_seq'),'SK','Slovakia',true),
(NEXTVAL('haikudepot.country_seq'),'SI','Slovenia',true),
(NEXTVAL('haikudepot.country_seq'),'SB','Solomon Islands',true),
(NEXTVAL('haikudepot.country_seq'),'SO','Somalia',true),
(NEXTVAL('haikudepot.country_seq'),'ZA','South Africa',true), -- clasquin-johnson.co.za
(NEXTVAL('haikudepot.country_seq'),'GS','South Georgia and the South Sandwich Islands',true),
(NEXTVAL('haikudepot.country_seq'),'SS','South Sudan',true),
(NEXTVAL('haikudepot.country_seq'),'ES','Spain',true),
(NEXTVAL('haikudepot.country_seq'),'LK','Sri Lanka',true),
(NEXTVAL('haikudepot.country_seq'),'SD','Sudan',true),
(NEXTVAL('haikudepot.country_seq'),'SR','Suriname',true),
(NEXTVAL('haikudepot.country_seq'),'SJ','Svalbard and Jan Mayen',true),
(NEXTVAL('haikudepot.country_seq'),'SZ','Swaziland',true),
(NEXTVAL('haikudepot.country_seq'),'SE','Sweden',true),
(NEXTVAL('haikudepot.country_seq'),'CH','Switzerland',true),
(NEXTVAL('haikudepot.country_seq'),'SY','Syrian Arab Republic',true),
(NEXTVAL('haikudepot.country_seq'),'TW','Taiwan, Province of China',true),
(NEXTVAL('haikudepot.country_seq'),'TJ','Tajikistan',true),
(NEXTVAL('haikudepot.country_seq'),'TZ','Tanzania, United Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'TH','Thailand',true),
(NEXTVAL('haikudepot.country_seq'),'TL','Timor-Leste',true),
(NEXTVAL('haikudepot.country_seq'),'TG','Togo',true),
(NEXTVAL('haikudepot.country_seq'),'TK','Tokelau',true),
(NEXTVAL('haikudepot.country_seq'),'TO','Tonga',true),
(NEXTVAL('haikudepot.country_seq'),'TT','Trinidad and Tobago',true),
(NEXTVAL('haikudepot.country_seq'),'TN','Tunisia',true),
(NEXTVAL('haikudepot.country_seq'),'TR','Turkey',true),
(NEXTVAL('haikudepot.country_seq'),'TM','Turkmenistan',true),
(NEXTVAL('haikudepot.country_seq'),'TC','Turks and Caicos Islands',true),
(NEXTVAL('haikudepot.country_seq'),'TV','Tuvalu',true),
(NEXTVAL('haikudepot.country_seq'),'UG','Uganda',true),
(NEXTVAL('haikudepot.country_seq'),'UA','Ukraine',true),
(NEXTVAL('haikudepot.country_seq'),'AE','United Arab Emirates',true),
(NEXTVAL('haikudepot.country_seq'),'GB','United Kingdom',true),
(NEXTVAL('haikudepot.country_seq'),'US','United States',true), -- coquillemartialarts.com
(NEXTVAL('haikudepot.country_seq'),'UM','United States Minor Outlying Islands',true),
(NEXTVAL('haikudepot.country_seq'),'UY','Uruguay',true),
(NEXTVAL('haikudepot.country_seq'),'UZ','Uzbekistan',true),
(NEXTVAL('haikudepot.country_seq'),'VU','Vanuatu',true),
(NEXTVAL('haikudepot.country_seq'),'VE','Venezuela, Bolivarian Republic of',true),
(NEXTVAL('haikudepot.country_seq'),'VN','Viet Nam',true),
(NEXTVAL('haikudepot.country_seq'),'VG','Virgin Islands, British',true),
(NEXTVAL('haikudepot.country_seq'),'VI','Virgin Islands, U.S.',true),
(NEXTVAL('haikudepot.country_seq'),'WF','Wallis and Futuna',true),
(NEXTVAL('haikudepot.country_seq'),'EH','Western Sahara',true),
(NEXTVAL('haikudepot.country_seq'),'YE','Yemen',true),
(NEXTVAL('haikudepot.country_seq'),'ZM','Zambia',true),
(NEXTVAL('haikudepot.country_seq'),'ZW','Zimbabwe',true);

INSERT INTO haikudepot.repository_source_mirror (
  id,
  code,
  repository_source_id,
  country_id,
  base_url,
  description,
  active,
  is_primary,
  create_timestamp,
  modify_timestamp)
  SELECT
    NEXTVAL('haikudepot.repository_source_mirror_seq'),
    rs.code || 'pri',
    rs.id,
    c.id,
    rs.url,
    NULL,
    true,
    true,
    now(),
    now()
  FROM
    haikudepot.repository_source rs
    JOIN haikudepot.country c ON c.code =
      CASE
        WHEN rs.url LIKE 'http://clasquin-johnson.co.za/%' THEN 'ZA'
        WHEN rs.url LIKE 'http://coquillemartialarts.com/%' THEN 'US'
        else 'DE'
      END;

ALTER TABLE haikudepot.repository_source DROP COLUMN url;

-- note that this is the identification url for the repository.  All
-- mirrors will carry this same identifier in the 'info.repo' file.
ALTER TABLE haikudepot.repository_source RENAME COLUMN repo_info_url TO url;
