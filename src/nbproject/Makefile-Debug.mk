#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc
CCC=g++
CXX=g++
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux-x86
CND_DLIB_EXT=so
CND_CONF=Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/EM.o \
	${OBJECTDIR}/assignMatrixValues.o \
	${OBJECTDIR}/calLD.o \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/mcPerm.o \
	${OBJECTDIR}/pairwiseMatrices.o \
	${OBJECTDIR}/pairwisePops.o \
	${OBJECTDIR}/readData.o \
	${OBJECTDIR}/run.o \
	${OBJECTDIR}/writeFile.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=-std=c++11
CXXFLAGS=-std=c++11

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/quantld

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/quantld: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/quantld ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/EM.o: nbproject/Makefile-${CND_CONF}.mk EM.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/EM.o EM.cpp

${OBJECTDIR}/assignMatrixValues.o: nbproject/Makefile-${CND_CONF}.mk assignMatrixValues.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/assignMatrixValues.o assignMatrixValues.cpp

${OBJECTDIR}/calLD.o: nbproject/Makefile-${CND_CONF}.mk calLD.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/calLD.o calLD.cpp

${OBJECTDIR}/main.o: nbproject/Makefile-${CND_CONF}.mk main.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/mcPerm.o: nbproject/Makefile-${CND_CONF}.mk mcPerm.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/mcPerm.o mcPerm.cpp

${OBJECTDIR}/pairwiseMatrices.o: nbproject/Makefile-${CND_CONF}.mk pairwiseMatrices.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/pairwiseMatrices.o pairwiseMatrices.cpp

${OBJECTDIR}/pairwisePops.o: nbproject/Makefile-${CND_CONF}.mk pairwisePops.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/pairwisePops.o pairwisePops.cpp

${OBJECTDIR}/readData.o: nbproject/Makefile-${CND_CONF}.mk readData.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/readData.o readData.cpp

${OBJECTDIR}/run.o: nbproject/Makefile-${CND_CONF}.mk run.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/run.o run.cpp

${OBJECTDIR}/writeFile.o: nbproject/Makefile-${CND_CONF}.mk writeFile.cpp 
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/include -I../Eigen3.22 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/writeFile.o writeFile.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}
	${RM} ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/quantld

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
