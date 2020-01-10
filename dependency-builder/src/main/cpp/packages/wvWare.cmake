include_guard(GLOBAL)

ExternalProjectAutotools(wvWare
  DEPENDS libgsf libpng libwmf

  URL https://www.abisource.com/downloads/wv/1.2.9/wv-1.2.9.tar.gz
  URL_HASH SHA256=4c730d3b325c0785450dd3a043eeb53e1518598c4f41f155558385dd2635c19d

  CONFIGURE_ARGUMENTS --with-libwmf=${THIRDPARTY_PREFIX}
)

