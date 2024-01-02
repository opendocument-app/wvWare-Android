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

cmake_minimum_required(VERSION 3.11.0 FATAL_ERROR)
set(CMAKE_BUILD_TYPE Release CACHE STRING "Build configuration (Debug, Release, RelWithDebInfo, MinSizeRel)")

project(wvWare-Android)

find_library(log-lib log)

add_definitions(-DHAVE_CONFIG_H)
add_library(wvware-android SHARED wvWare-Android.cpp
        CCharGC.h
        wvWare.c
        errorPrinter.h
)

find_package(tmpfile REQUIRED CONFIG)
find_package(wvWare REQUIRED CONFIG)

target_link_libraries(wvware-android
  wvWare::wv
  tmpfile::tmpfile
  ${log-lib}
)

get_target_property(GSF_INCLUDE_DIR libgsf::gsf-1 INTERFACE_INCLUDE_DIRECTORIES)
get_target_property(GLIB2_INCLUDE_DIR glib2::glib-2.0 INTERFACE_INCLUDE_DIRECTORIES)
get_target_property(FT_INCLUDE_DIR freetype::freetype INTERFACE_INCLUDE_DIRECTORIES)
target_include_directories(wvware-android SYSTEM AFTER
  PUBLIC ${GSF_INCLUDE_DIR}/libgsf-1
  PUBLIC ${GLIB2_INCLUDE_DIR}/glib-2.0
  PUBLIC ${FT_INCLUDE_DIR}/freetype2
)
