package(default_visibility = ["//visibility:public"])

java_library(
  name = "gatelib",
  srcs = glob(["src/**/*.java", "src/*.java"]),
  deps = [
    "@duckutil//:duckutil_lib",
  ],
)

java_binary(
  name = "GateControl",
  main_class = "duckutil.gatecontrol.GateControl",
  runtime_deps = [
    ":gatelib",
  ],
)

