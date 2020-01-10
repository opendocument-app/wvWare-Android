include_guard(GLOBAL)

ExternalProjectMeson(glib-2.0
  DEPENDS iconv intl libffi zlib
  URL https://download.gnome.org/sources/glib/2.62/glib-2.62.4.tar.xz
  URL_HASH SHA256=4c84030d77fa9712135dfa8036ad663925655ae95b1d19399b6200e869925bbc
  CONFIGURE_ARGUMENTS -Dlibmount=false
)

