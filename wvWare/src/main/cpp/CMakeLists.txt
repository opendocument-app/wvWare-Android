# CMakeLists.txt
#
# Copyright (C) 2020, 2024 Vilius Sutkus'89 <ViliusSutkus89@gmail.com>
#
# wvWare-Android is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 3 as published by
# the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

find_library(log-lib log)

add_definitions(-DHAVE_CONFIG_H)
add_library(wvware-android SHARED wvWare-Android.cpp
        CCharGC.h
        EnvVar.c
)

find_package(wvWare REQUIRED CONFIG)

target_link_libraries(wvware-android
  wvware::wvware
  ${log-lib}
)
