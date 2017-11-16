cmake_minimum_required(VERSION 3.8)
project(CLion_Proj)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp)
add_executable(CLion_Proj ${SOURCE_FILES})

include_directories(ext/glfw/include)