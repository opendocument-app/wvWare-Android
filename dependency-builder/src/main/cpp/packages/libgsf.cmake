include_guard(GLOBAL)

#@TODO: enable largefile for android-24 ? or Android64?
#https://android.googlesource.com/platform/bionic/+/master/docs/32-bit-abi.md

ExternalProjectAutotools(libgsf
  DEPENDS glib-2.0 libxml-2.0 zlib

  URL https://ftp.gnome.org/pub/GNOME/sources/libgsf/1.14/libgsf-1.14.46.tar.xz
  URL_HASH SHA256=ea36959b1421fc8e72caa222f30ec3234d0ed95990e2bf28943a85f33eadad2d

  CONFIGURE_ARGUMENTS --disable-largefile
)

