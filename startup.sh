#!/bin/bash

# Get the Project Directory - from https://stackoverflow.com/questions/4774054/reliable-way-for-a-bash-script-to-get-the-full-path-to-itself
export PROJECT_DIRECTORY="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

# Get the user Supercollider Quarks Directory
export USER=`whoami`
export QUARKS_DIRECTORY="/home/${USER}/.local/share/SuperCollider/ALGORITHM_MIXER_QUARKS"
export QUARKS_DIRECTORY_SED=$(echo $QUARKS_DIRECTORY | sed 's_/_\\/_g')

echo "$QUARKS_DIRECTORY"

# Get local extension/classes directory
export LOCAL_EXTENSION_DIRECTORY=${PROJECT_DIRECTORY}/extensions
export LOCAL_EXTENSION_DIRECTORY_SED=$(echo $LOCAL_EXTENSION_DIRECTORY | sed 's_/_\\/_g')

export LOCAL_SOURCE_DIRECTORY=${PROJECT_DIRECTORY}/src
export LOCAL_SOURCE_DIRECTORY_SED=$(echo $LOCAL_SOURCE_DIRECTORY | sed 's_/_\\/_g')

# Get local plugins (UGen Objects) directory
export SYSTEM_PLUGINS_DIRECTORY="/usr/lib64/SuperCollider/plugins/"
export LOCAL_PLUGINS_DIRECTORY=${LOCAL_EXTENSION_DIRECTORY}/plugins/lib

# Go to the project directory so all relative paths are appropriately set
cd $PROJECT_DIRECTORY

# Generate the YAML Configuration File
export YAML_CONFIG="/tmp/ALGORITHM_MIXER.yaml"
cp conf_base.yaml $YAML_CONFIG

# Generate the vim syntax and tags directories
export SCNVIM_TAGS_DIRECTORY=${PROJECT_DIRECTORY}/tags
mkdir -p ${SCNVIM_TAGS_DIRECTORY}/scnvim-data
mkdir -p ${SCNVIM_TAGS_DIRECTORY}/syntax

# Update the YAML

sed -i s/QUARKS_DIRECTORY/${QUARKS_DIRECTORY_SED}/g $YAML_CONFIG
sed -i s/LOCAL_EXTENSION_DIRECTORY/${LOCAL_EXTENSION_DIRECTORY_SED}/g $YAML_CONFIG
sed -i s/LOCAL_SOURCE_DIRECTORY/${LOCAL_SOURCE_DIRECTORY_SED}/g $YAML_CONFIG

# Source the vim environment bootstrap when neovim starts
/usr/bin/nvim -c ":source environment.vim"
