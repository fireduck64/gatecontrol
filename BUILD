package(default_visibility = ["//visibility:public"])

java_library(
  name = "gatelib",
  srcs = glob(["src/**/*.java", "src/*.java"]),
  deps = [
    "@duckutil//:duckutil_lib",
    "@duckutil//:duckutil_jsonrpc_lib",
    "@maven//:net_minidev_json_smart",
    "@maven//:com_thetransactioncompany_jsonrpc2_server",
    "@maven//:com_thetransactioncompany_jsonrpc2_base",
  ],
)

java_binary(
  name = "GateControl",
  main_class = "duckutil.gatecontrol.GateControl",
  runtime_deps = [
    ":gatelib",
  ],
)


