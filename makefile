.PHONY: deploy

deploy:
	@echo "Deploying the application..."
	(cd dragon-ui && $(MAKE) build-ui)
	./mvnw clean install
	scp install-on-pi.sh dragon:~
	scp target/dragon-control-*.jar dragon:~/dragon-control.jar
	scp dragon.service dragon:~
	ssh dragon './install-on-pi.sh'

	echo "Try ssh dragon 'sudo journalctl --unit=dragon.service --follow' to see the logs."
	osascript -e "tell application \"iTerm\" to create window with default profile command \"ssh dragon 'sudo journalctl --unit=dragon.service --follow'\""

shutdown-pi:
	ssh dragon 'sudo shutdown now'

reboot-pi:
	ssh dragon 'sudo reboot'

connect-jbl:
	ssh dragon 'bluetoothctl connect 68:52:10:2F:9C:1A'

browser:
	open 'http://192.168.86.7:8080/dragon'