#!/bin/bash
# set colors
blue=$(tput setaf 4)
red=$(tput setaf 1)
green=$(tput setaf 2)
reset=$(tput sgr0)

# Function to display the menu
function show_menu() {
  clear
  echo "${blue} Select an option:"
  for i in "${!options[@]}"; do
    if [ "$i" -eq "$selected_option" ]; then
      echo "> ${options[$i]} <"
    else
      echo "  ${options[$i]}"
    fi
  done
}

options=("1- Start all services" "2- Exiting")
selected_option=0

while true; do
  show_menu

  # Read user input (arrow keys)
  read -rsn1 input

  case "$input" in
    $'\e') # Escape sequence for arrow keys
      read -rsn2 -t 0.1 input # Read the next two characters
      if [[ "$input" == "[A" ]]; then # Up arrow
        ((selected_option--))
        if [ "$selected_option" -lt 0 ]; then selected_option=0; fi
      elif [[ "$input" == "[B" ]]; then # Down arrow
        ((selected_option++))
        if [ "$selected_option" -ge "${#options[@]}" ]; then selected_option=$((${#options[@]}-1)); fi
      fi
      ;;
    '') # Enter key pressed
      break
      ;;
  esac
done

case "${options[$selected_option]}" in
  "1- Start all services")
    db_options=("1- Start services and build images" "2- Restart all containers and clear database")
    selected_db_option=0

    while true; do
      clear
      echo "Select an option :"
      for i in "${!db_options[@]}"; do
        if [ "$i" -eq "$selected_db_option" ]; then
          echo "> ${db_options[$i]} <"
        else
          echo "  ${db_options[$i]}"
        fi
      done

      read -rsn1 input

      case "$input" in
        $'\e')
          read -rsn2 -t 0.1 input
          if [[ "$input" == "[A" ]]; then
            ((selected_db_option--))
            if [ "$selected_db_option" -lt 0 ]; then selected_db_option=0; fi
          elif [[ "$input" == "[B" ]]; then
            ((selected_db_option++))
            if [ "$selected_db_option" -ge "${#db_options[@]}" ]; then selected_db_option=$((${#db_options[@]}-1)); fi
          fi
          ;;
        '')
          break
          ;;
      esac
    done

    case "${db_options[$selected_db_option]}" in
      "2- Restart all containers and clear database")
        echo "${red}Stopping services...${reset}"
        docker-compose down && docker-compose up --build
        ;;
      "1- Start services and build images")
        echo "${green}Starting services and building images...${reset}"
        docker-compose up --build
        ;;
    esac

    ;;
  "2- Exiting")
    echo "${red}No action taken.${reset}"
    ;;
esac

echo "${reset} Exiting."
