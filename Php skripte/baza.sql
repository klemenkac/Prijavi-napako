-- phpMyAdmin SQL Dump
-- version 4.6.6
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 16, 2017 at 07:43 PM
-- Server version: 10.1.20-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id1701907_prijavinapako`
--

-- --------------------------------------------------------

--
-- Table structure for table `napaka`
--

CREATE TABLE `napaka` (
  `id` varchar(35) COLLATE utf8_unicode_ci NOT NULL,
  `dom` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `soba` varchar(4) COLLATE utf8_unicode_ci NOT NULL,
  `tip_napake` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `opis` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `user` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `slika` longtext COLLATE utf8_unicode_ci NOT NULL,
  `datum` varchar(12) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `napaka`
--

INSERT INTO `napaka` (`id`, `dom`, `soba`, `tip_napake`, `opis`, `user`, `slika`, `datum`) VALUES
('60bcff84c51349d697c9f264f23fd2b2', 'Dom 3', '122', 'Internet', 'Ni wifija...', 'Klemen Kac', '/storage/emulated/0/Pictures/MAGICAL CAMERA/myPhotoName_20170621123146.png', '21. 06. 2017'),
('b6e0ff7acdcb41e6af77602bb6186977', 'Dom 3', '669', 'Ogrevanje', 'ni ogrrvanja', 'Klemen Kac', '/storage/emulated/0/Pictures/MAGICAL CAMERA/myPhotoName_20170913151635.png', '13. 09. 2017');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `e_naslov` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `sifra` int(6) NOT NULL,
  `geslo` varchar(16) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `e_naslov`, `sifra`, `geslo`) VALUES
(24, 'asd', 5, 'asd'),
(25, 'asd', 5, 'asd'),
(26, '64', 64, 'zhjs'),
(27, 'dasd', 21, 'fsd'),
(28, 'fd', 2, 'fds'),
(29, 'fd', 2, 'ert'),
(30, 'admin', 45646, 'admin'),
(31, 'lol@gmail.com', 676046, 'lol'),
(32, 'admin@admin.com', 1234, '1234'),
(33, 'xklemenx@gmail.com', 12, '1'),
(34, 'xklemenx@gmail.com', 12, '1'),
(35, 'xklemenx@gmail.com', 12, '1'),
(36, 'xklemenx@gmail.com', 12, '1'),
(37, 'xklemenx@gmail.com', 12, '1'),
(38, 'a', 321, 'a');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `napaka`
--
ALTER TABLE `napaka`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=39;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
