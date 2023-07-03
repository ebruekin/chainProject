-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: 127.0.0.1
-- Üretim Zamanı: 03 Tem 2023, 02:26:03
-- Sunucu sürümü: 10.4.28-MariaDB
-- PHP Sürümü: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `donatersys`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `request`
--

CREATE TABLE `request` (
  `ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `HEADER` varchar(250) NOT NULL,
  `MESSAGE` varchar(500) NOT NULL,
  `DONOR_PAY` varchar(20) NOT NULL,
  `APPROVAL_STATUS` int(11) NOT NULL,
  `PAYMENT_STATUS` int(11) NOT NULL,
  `DONATE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

--
-- Tablo döküm verisi `request`
--

INSERT INTO `request` (`ID`, `USER_ID`, `HEADER`, `MESSAGE`, `DONOR_PAY`, `APPROVAL_STATUS`, `PAYMENT_STATUS`, `DONATE`) VALUES
(1, 6, 'test', '123', '123', 1, 0, 0),
(2, 6, 'test', '213', '147', 0, 0, 0);

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `TC` varchar(250) NOT NULL,
  `EMAİL` varchar(250) DEFAULT NULL,
  `PASSWORD` varchar(250) NOT NULL,
  `DOC` varchar(250) DEFAULT NULL,
  `ROLE` varchar(2) NOT NULL,
  `ACTİVE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_turkish_ci;

--
-- Tablo döküm verisi `users`
--

INSERT INTO `users` (`ID`, `TC`, `EMAİL`, `PASSWORD`, `DOC`, `ROLE`, `ACTİVE`) VALUES
(3, '147', 'admin@admin.com', '6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2', NULL, '0', 1),
(6, '11111111111', 'ogrenci@d.com', '6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2', '2598', '1', 1),
(7, '11111231321313213', 'bagis@d.com', '6216f8a75fd5bb3d5f22b6f9958cdede3fc086c2', NULL, '2', 1);

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `request`
--
ALTER TABLE `request`
  ADD PRIMARY KEY (`ID`);

--
-- Tablo için indeksler `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `request`
--
ALTER TABLE `request`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Tablo için AUTO_INCREMENT değeri `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
