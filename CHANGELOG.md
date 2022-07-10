# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/) and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).
Prior to version 6.0.0, this project used MCVERSION-MAJORMOD.MAJORAPI.MINOR.PATCH.

## [6.0.0-beta.2+1.19] - 2022.07.10
### Changed
- [Forge] Updated to and requires Forge 41.0.64 or above

## [6.0.0-beta.1+1.19] - 2022.07.06
### Added
- Added Waterfall pattern compatibility from Additional Banners
### Changed
- Merged Forge and Fabric versions of the project together using the [MultiLoader template](https://github.com/jaredlll08/MultiLoader-Template)
- Changed to [Semantic Versioning](http://semver.org/spec/v2.0.0.html)
- Updated to Minecraft 1.19
- [Forge] Updated to Forge 41+
- [Fabric] Updated to Fabric API 0.55.2+

## [1.18.2-5.1.2.0] - 2022.07.04
### Added
- Added Squares, Hexagon, and Frame pattern compatibility from Additional Banners
### Fixed
- Fixed Clubs pattern compatibility from Additional Banners

## [1.18.2-5.1.1.0] - 2022.07.04
### Added
- Added compatibility with Alex's Mobs banner patterns

## [1.18.1-5.1.0.5] - 2022.01.26
### Fixed
- Fixed crash on Forge 39.0.45+
    - Important: This fix will cause all placed decorated beds to lose their patterns. Please pick up any decorated beds
      that should keep their patterns before updating.

## [1.18-5.1.0.4] - 2021.12.08
### Added
- Added es_ar.json and es_mx.json localization files (thanks feliixpe!) [#17](https://github.com/TheIllusiveC4/Bedspreads/pull/17)
### Changed
- Updated to Minecraft 1.18
- Updated to Forge 38+

## [1.17.1-5.1.0.3] - 2021.09.26
### Changed
- Updated to Minecraft 1.17.1
- Updated to Forge 37.0.59+

## [1.16.5-5.1.0.3] - 2021.09.26
### Added
- Added Spanish localization (thanks zeedif!) [#16](https://github.com/TheIllusiveC4/Bedspreads/pull/16)

## [1.16.5-5.1.0.2] - 2021.04.01
### Fixed
- Fixed rendering crash [#15](https://github.com/TheIllusiveC4/Bedspreads/issues/15)

## [1.16.4-5.1.0.1] - 2020.11.21
### Fixed
- Fixed Optifine crash [#14](https://github.com/TheIllusiveC4/Bedspreads/issues/14)

## [1.16.4-5.1.0.0] - 2020.11.20
### Added
- Added Additional Banners integration
- Added Ice and Fire: Dragons integration
- Added Rats integration
- Added French localization (thanks Lykrast!) [#10](https://github.com/TheIllusiveC4/Bedspreads/pull/10)
- Added Russian localization (thanks BardinTheDwarf!) [#8](https://github.com/TheIllusiveC4/Bedspreads/pull/8)
### Changed
- Updated to Minecraft 1.16.4
### Fixed
- Fixed villagers not recognizing decorated beds as valid beds

## [1.16.3-5.0.0.1] - 2020.09.29
### Changed
- Updated to Minecraft 1.16.3

## [1.16.2-5.0.0.0] - 2020.08.17
### Added
- Added Chinese localization (thanks qsefthuopq!) [#7](https://github.com/TheIllusiveC4/Bedspreads/pull/7)
### Changed
- Updated to Minecraft 1.16.2

## [1.12.2-4.0.0.2] - 2020.08.05
### Added
- Added Croatian localization (thanks COMBOhrenovke!) [#6](https://github.com/TheIllusiveC4/Bedspreads/pull/6)

## [1.12.2-4.0.0.1] - 2020.08.03
### Changed
- Updated texture blending and compression

## [1.12.2-4.0.0.0] - 2020.07.04
### Changed
- Ported to 1.16.1 Forge

## [1.12.2-3.0.0.1] - 2020.04.24
### Fixed
- Fixed texture issues with gradient patterns and half horizontal patterns

## [1.12.2-3.0.0.0] - 2020.02.14
### Changed
- Ported to 1.15.2 Forge
- Changed mod name from Cosmetic Beds to Bedspreads
- Decorated bed textures are slightly modified due to changes to Minecraft's rendering system

## [1.12.2-2.0.0.1] - 2019.11.18
### Added
- Added Globe pattern
### Fixed
- Fixed missing patterns resulting in no texture

## [1.12.2-2.0.0.0] - 2019.09.16
### Changed
- Ported to 1.14.4
### Removed
- Removed Quark and Additional Banners support until those mods are available again

## [1.12.2-2.0.0.0-beta2] - 2019.08.11
### Fixed
- Fixed server-side crash [#4](https://github.com/illusivesoulworks/bedspreads/issues/4)

## [1.12.2-2.0.0.0-beta1] - 2019.05.14
### Changed
- Ported to 1.13.2 Forge
- Merged all Decorated Bed colored variants into a single item which still retains color information

## [1.12.2-1.0.2.2] - 2019.02.01
### Fixed
- Cosmetic beds losing tile entity data after sleeping

## [1.12.2-1.0.2.1] - 2019.02.01
### Fixed
- Dropped/broken cosmetic beds not retaining the correct banner color

## [1.12.2-1.0.2.0] - 2019.01.04
### Added
- Additional Banners banner integration

## [1.12.2-1.0.1.0] - 2018.12.17
### Added
- Quark banner integration [#2](https://github.com/illusivesoulworks/bedspreads/issues/2)
### Fixed
- Bed rendering errors when applying modded banners that aren't supported yet, or otherwise don't exist [#2](https://github.com/TheIllusiveC4/CosmeticBeds/issues/2)

## [1.12.2-1.0.0.0] - 2018.12.09
- Initial release