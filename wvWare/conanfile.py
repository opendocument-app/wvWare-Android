import os

from conan import ConanFile
from conan.tools.cmake import CMakeToolchain, CMakeDeps

required_conan_version = ">=2.0.6"


class wvWareConan(ConanFile):
    settings = "os", "compiler", "build_type", "arch"
    requires = "wvware/1.2.9"

    def generate(self):
        deps = CMakeDeps(self)
        deps.generate()
        tc = CMakeToolchain(self)
        tc.generate()

        asset_dir = os.path.join(self.build_folder, 'assets')
        os.mkdir(asset_dir)
        os.symlink(self.dependencies['wvware'].cpp_info.resdirs[0], os.path.join(asset_dir, 'wv'))
