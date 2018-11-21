suite = {
  "mxversion" : "5.175.4",
  "name" : "core",
  "versionConflictResolution" : "latest",

  "javac.lint.overrides" : "none",

  "licenses" : {
    "GPLv3" : {
      "name" : "GNU General Public License, version 3",
      "url" : "https://opensource.org/licenses/GPL-3.0",
    },
    "LGPLv3" : {
      "name" : "GNU Lesser General Public License, version 3",
      "url" : "https://opensource.org/licenses/LGPL-3.0",
    },
  },

  "defaultLicense" : "LGPLv3",

  "projects" : {
    "com.everyware.util" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "checkstyle" : "com.everyware.util",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.math" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "checkstyle" : "com.everyware.math",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.xml" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.util"
      ],
      "checkstyle" : "com.everyware.xml",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.posix" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.util"
      ],
      "checkstyle" : "com.everyware.posix",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.util.test" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.util",
        "mx:JUNIT",
      ],
      "checkstyle" : "com.everyware.util",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.math.test" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.math",
        "mx:JUNIT",
      ],
      "checkstyle" : "com.everyware.math",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.xml.test" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.xml",
        "mx:JUNIT",
      ],
      "checkstyle" : "com.everyware.xml",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },

    "com.everyware.posix.test" : {
      "subDir" : "projects",
      "sourceDirs" : ["src"],
      "dependencies" : [
        "com.everyware.posix",
        "mx:JUNIT",
      ],
      "checkstyle" : "com.everyware.posix",
      "javaCompliance" : "1.8+",
      "workingSets" : "core",
    },
  },

  "distributions" : {
    "CORE" : {
      "path" : "build/core.jar",
      "subDir" : "core",
      "sourcesPath" : "build/core.src.zip",
      "dependencies" : [
        "com.everyware.util",
        "com.everyware.math",
        "com.everyware.xml",
      ]
    },

    "POSIX" : {
      "path" : "build/posix.jar",
      "subDir" : "core",
      "sourcesPath" : "build/posix.src.zip",
      "dependencies" : [
        "com.everyware.posix",
      ],
      "distDependencies" : [
        "CORE"
      ]
    },

    "CORE_TEST" : {
      "path" : "build/core_test.jar",
      "subDir" : "core",
      "sourcesPath" : "build/core_test.src.zip",
      "dependencies" : [
        "com.everyware.util.test",
        "com.everyware.math.test",
        "com.everyware.xml.test",
      ],
      "exclude" : [
        "mx:JUNIT"
      ],
      "distDependencies" : [
        "core:CORE",
      ]
    },

    "POSIX_TEST" : {
      "path" : "build/posix_test.jar",
      "subDir" : "core",
      "sourcesPath" : "build/posix_test.src.zip",
      "dependencies" : [
        "com.everyware.posix.test"
      ],
      "exclude" : [
        "mx:JUNIT"
      ],
      "distDependencies" : [
        "core:CORE",
        "core:POSIX"
      ]
    }
  }
}
