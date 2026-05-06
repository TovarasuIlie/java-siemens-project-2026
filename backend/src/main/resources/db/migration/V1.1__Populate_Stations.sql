INSERT INTO stations (name, city, type) VALUES
('Iași', 'Iași', 'GARA_PRINCIPALA'),
('Târgu Frumos', 'Târgu Frumos', 'STATION'),
('Pașcani', 'Pașcani', 'STATION'),
('Dolhasca', 'Dolhasca', 'STATION'),
('Verești', 'Verești', 'STATION'),
('Suceava', 'Suceava', 'GARA_PRINCIPALA'),
('Gura Humorului Oraș', 'Gura Humorului', 'STATION'),
('Frasin', 'Frasin', 'STATION'),
('Vama', 'Vama', 'STATION'),
('Câmpulung Est', 'Câmpulung Moldovenesc', 'STATION'),
('Câmpulung Moldovenesc', 'Câmpulung Moldovenesc', 'STATION'),
('Pojorâta', 'Pojorâta', 'STATION'),
('Mestecăniș', 'Iacobeni', 'STATION'),
('Iacobeni', 'Iacobeni', 'STATION'),
('Vatra Dornei', 'Vatra Dornei', 'STATION'),
('Vatra Dornei Băi h', 'Vatra Dornei', 'HALTA'),
('Lunca Ilvei', 'Lunca Ilvei', 'STATION'),
('Măgura Ilvei', 'Măgura Ilvei', 'STATION'),
('Ilva Mică', 'Ilva Mică', 'STATION'),
('Năsăud', 'Năsăud', 'STATION'),
('Salva', 'Salva', 'STATION'),
('Beclean pe Someș', 'Beclean', 'STATION'),
('Dej Călători', 'Dej', 'STATION'),
('Gherla', 'Gherla', 'STATION'),
('Cluj Napoca', 'Cluj-Napoca', 'GARA_PRINCIPALA'),
('Câmpia Turzii', 'Câmpia Turzii', 'STATION'),
('Războieni', 'Războieni', 'STATION'),
('Aiud', 'Aiud', 'STATION'),
('Teiuș', 'Teiuș', 'STATION'),
('Alba Iulia', 'Alba Iulia', 'GARA_PRINCIPALA'),
('Vințu de Jos', 'Vințu de Jos', 'STATION'),
('Șibot', 'Șibot', 'STATION'),
('Oraștie', 'Oraștie', 'STATION'),
('Simeria', 'Simeria', 'GARA_PRINCIPALA'),
('Deva', 'Deva', 'STATION'),
('Ilia', 'Ilia', 'STATION'),
('Margina', 'Margina', 'STATION'),
('Făget', 'Făget', 'STATION'),
('Mănăștur', 'Mănăștur', 'STATION'),
('Lugoj', 'Lugoj', 'STATION'),
('Buziaș', 'Buziaș', 'STATION'),
('Timișoara Nord', 'Timișoara', 'GARA_PRINCIPALA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Timișoara Nord', 'Timișoara', 'GARA_PRINCIPALA'),
('Buziaș', 'Buziaș', 'STATION'),
('Lugoj', 'Lugoj', 'STATION'),
('Mănăștur', 'Mănăștur', 'STATION'),
('Făget', 'Făget', 'STATION'),
('Margina', 'Margina', 'STATION'),
('Ilia', 'Ilia', 'STATION'),
('Deva', 'Deva', 'STATION'),
('Simeria', 'Simeria', 'GARA_PRINCIPALA'),
('Oraștie', 'Oraștie', 'STATION'),
('Șibot', 'Șibot', 'STATION'),
('Vințu de Jos', 'Vințu de Jos', 'STATION'),
('Alba Iulia', 'Alba Iulia', 'GARA_PRINCIPALA'),
('Teiuș', 'Teiuș', 'STATION'),
('Aiud', 'Aiud', 'STATION'),
('Războieni', 'Războieni', 'STATION'),
('Câmpia Turzii', 'Câmpia Turzii', 'STATION'),
('Cluj Napoca', 'Cluj-Napoca', 'GARA_PRINCIPALA'),
('Gherla', 'Gherla', 'STATION'),
('Dej Călători', 'Dej', 'STATION'),
('Beclean pe Someș', 'Beclean', 'STATION'),
('Salva', 'Salva', 'STATION'),
('Năsăud', 'Năsăud', 'STATION'),
('Ilva Mică', 'Ilva Mică', 'STATION'),
('Măgura Ilvei', 'Măgura Ilvei', 'STATION'),
('Lunca Ilvei', 'Lunca Ilvei', 'STATION'),
('Vatra Dornei Băi h', 'Vatra Dornei', 'HALTA'),
('Vatra Dornei', 'Vatra Dornei', 'STATION'),
('Iacobeni', 'Iacobeni', 'STATION'),
('Mestecăniș', 'Iacobeni', 'STATION'),
('Pojorâta', 'Pojorâta', 'STATION'),
('Câmpulung Moldovenesc', 'Câmpulung Moldovenesc', 'STATION'),
('Câmpulung Est', 'Câmpulung Moldovenesc', 'STATION'),
('Vama', 'Vama', 'STATION'),
('Frasin', 'Frasin', 'STATION'),
('Gura Humorului Oraș', 'Gura Humorului', 'STATION'),
('Suceava', 'Suceava', 'GARA_PRINCIPALA'),
('Verești', 'Verești', 'STATION'),
('Dolhasca', 'Dolhasca', 'STATION'),
('Pașcani', 'Pașcani', 'STATION'),
('Târgu Frumos', 'Târgu Frumos', 'STATION'),
('Iași', 'Iași', 'GARA_PRINCIPALA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Roșu Hm', 'Vatra Dornei', 'HALTA'),
('Dorna Candrenilor hc', 'Dorna Candrenilor', 'HALTA'),
('Floreni', 'Floreni', 'STATION'),
('Coșna Hm', 'Coșna', 'HALTA'),
('Grădinița', 'Grădinița', 'STATION'),
('Larion Hm', 'Larion', 'HALTA'),
('Silhoasa Hm', 'Silhoasa', 'HALTA'),
('Ilva Mare', 'Ilva Mare', 'STATION'),
('Poiana Ilvei', 'Poiana Ilvei', 'HALTA'),
('Leșu Ilvei Hm', 'Leșu Ilvei', 'HALTA'),
('Strâmba h', 'Strâmba', 'HALTA'),
('Podereia Feldrului h.', 'Feldru', 'HALTA'),
('Feldru Hm', 'Feldru', 'HALTA'),
('Nepos hc', 'Nepos', 'HALTA'),
('Rebrișoara', 'Rebrișoara', 'STATION'),
('Năsăud H', 'Năsăud', 'HALTA'),
('Mititei hc', 'Mititei', 'HALTA'),
('Nimigea', 'Nimigea', 'STATION'),
('Nimigea hc', 'Nimigea', 'HALTA'),
('Mogoșeni Hm', 'Mogoșeni', 'HALTA'),
('Cociu hc', 'Cociu', 'HALTA'),
('Șintereag', 'Șintereag', 'STATION'),
('Caila hc', 'Caila', 'HALTA'),
('Sfântu hc', 'Sfântu', 'HALTA'),
('Măgheruș Șieu', 'Măgheruș Șieu', 'STATION'),
('Arcalia hc', 'Arcalia', 'HALTA'),
('Sărățel', 'Sărățel', 'STATION'),
('Sărata hc', 'Sărata', 'HALTA'),
('Viișoara h', 'Viișoara', 'HALTA'),
('Bistrița Nord', 'Bistrița', 'GARA_PRINCIPALA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Clujana h', 'Cluj-Napoca', 'HALTA'),
('Cluj Napoca Est', 'Cluj-Napoca', 'STATION'),
('Dezmir hc', 'Dezmir', 'HALTA'),
('Apahida', 'Apahida', 'STATION'),
('Apahida h', 'Apahida', 'HALTA'),
('Jucu', 'Jucu', 'STATION'),
('Răscruci hc', 'Răscruci', 'HALTA'),
('Bonțida', 'Bonțida', 'STATION'),
('Fundătura hc', 'Fundătura', 'HALTA'),
('Iclod', 'Iclod', 'STATION'),
('Livada Someș hc', 'Livada Someșului', 'HALTA'),
('Bunești h', 'Bunești', 'HALTA'),
('Nima hc', 'Nima', 'HALTA'),
('Dej', 'Dej', 'STATION'),
('Reteag', 'Reteag', 'STATION'),
('Reteag hc', 'Reteag', 'HALTA'),
('Ciceu Cristur hc', 'Ciceu Cristur', 'HALTA'),
('Coldău', 'Coldău', 'STATION')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Bistrița Fabrici h.', 'Bistrița', 'HALTA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Baia Mare', 'Baia Mare', 'GARA_PRINCIPALA'),
('Satulung pe Someș', 'Satulung', 'STATION'),
('Fersig hc', 'Fersig', 'HALTA'),
('Mireșu Mare hm', 'Mireșu Mare', 'HALTA'),
('Ulmeni Sălaj', 'Ulmeni', 'STATION'),
('Țicău hc', 'Țicău', 'HALTA'),
('Benesat', 'Benesat', 'STATION'),
('Aluniș Sălaj hc', 'Aluniș', 'HALTA'),
('Inău hc', 'Inău', 'HALTA'),
('Someș Odorhei', 'Someș Odorhei', 'STATION'),
('Jibou', 'Jibou', 'GARA_PRINCIPALA'),
('Surduc Sălaj', 'Surduc', 'STATION'),
('Ciocmani hc', 'Ciocmani', 'HALTA'),
('Băbuțeni', 'Băbuțeni', 'STATION'),
('Cuciulat', 'Cuciulat', 'STATION'),
('Letca', 'Letca', 'STATION'),
('Răstoci', 'Răstoci', 'STATION'),
('Ileanda', 'Ileanda', 'STATION'),
('Gâlgău', 'Gâlgău', 'STATION'),
('Cășeiu Hm', 'Cășeiu', 'HALTA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Jucu de Jos hc', 'Jucu', 'HALTA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Nicolina', 'Iași', 'STATION'),
('Buhăiești', 'Buhăiești', 'STATION'),
('Vaslui', 'Vaslui', 'GARA_PRINCIPALA'),
('Crasna', 'Crasna', 'STATION'),
('Bârlad', 'Bârlad', 'GARA_PRINCIPALA'),
('Tecuci', 'Tecuci', 'GARA_PRINCIPALA'),
('Liești', 'Liești', 'STATION'),
('Barboși', 'Barboși', 'STATION'),
('Galați', 'Galați', 'GARA_PRINCIPALA')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Aradu Nou', 'Arad', 'STATION'),
('Radna', 'Lipova', 'STATION'),
('Săvârșin', 'Săvârșin', 'STATION')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Bârnova', 'Bârnova', 'STATION')
ON CONFLICT (name) DO NOTHING;

INSERT INTO stations (name, city, type) VALUES
('Vatra Dornei Prut', 'Vatra Dornei', 'HALTA'),
('Poiana Stampei', 'Poiana Stampei', 'STATION'),
('Dornișoara', 'Dornișoara', 'STATION')
ON CONFLICT (name) DO NOTHING;