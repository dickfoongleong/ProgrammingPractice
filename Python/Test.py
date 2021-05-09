ErrorData = "Please insert a valid value."

Menu = "\nBlackwater Annual Music Concert\n" \
           "---------------------------------\n" \
           "1. Adding Performers\n" \
           "2. Generate Concert Details\n" \
           "3. Quit"

Add_Menu = "\n(1) Adding Performers\n" \
                 "----------------------------------"

PerformanceMenu = "Type of Performance\n\t" \
                          "1. Musician\n\t" \
                          "2. Singer\n\t" \
                          "3. Dancer"

MUSICIAN = "Musician"
SINGER = "Singer"
DANCER = "Dancer"

def addPerformers():
    longestPerformerName = ""
    longestPerformerDuration = 0
    longestPerformerType = ""

    totalTime = 0
    musicianCount = 0
    dancerCount = 0
    singerCount = 0
    performerList = ""
    performerRecords = ""

    with open("performer.txt", "a") as output:
        print(Add_Menu)
        performerCounts = int(input("How many performers are you adding: "))
        for i in range(performerCounts):
            print(f"\nPerformers {i+1} / {performerCounts}")

            # First and last name
            fName = str(input("Enter your name: "))
            lName = str(input("Enter your surname: "))

            # Type of performance
            performanceOption = 0
            performanceType = ""
            while performanceOption != 1 and performanceOption != 2 and performanceOption != 3:
                print(PerformanceMenu)
                performanceOption = int(input("==> "))
            if performanceOption == 1:
                performanceType = MUSICIAN
                musicianCount += 1
            elif performanceOption == 2:
                performanceType = SINGER
                singerCount += 1
            elif performanceOption == 3:
                performanceType = DANCER
                dancerCount += 1

            # Duration in mins for performance
            duration = int(input("Time slot required (mins): "))
            totalTime += duration

            # Add current performer to the list
            performerList += f"{i+1}. {lName},{fName: <15}\t{performanceType: <15}{duration} minutes\n"
            output.write(f"{lName} {fName} {performanceType} {duration}\n")

            # Decide the longest duration performer
            if duration > longestPerformerDuration:
                longestPerformerName = f"{fName} {lName}"
                longestPerformerType = performanceType
                longestPerformerDuration = duration

        # Print added details and summary
        print("\nThe following information has been added.\n")
        print(performerList)
        print("Summary Notes:\n-------------")
        print(f"{musicianCount} {MUSICIAN}s")
        print(f"{singerCount} {SINGER}s")
        print(f"{dancerCount} {DANCER}s")
        print(f"Total time required: {totalTime // 60} hour(s) {totalTime % 60} min(s).")
        print(f"The longest act added is {longestPerformerName} ({longestPerformerType}) {longestPerformerDuration} minutes.\n")

def showDetails():
    #Show input details
    print()
    with open("performer.txt", "r") as data_file:
        counter = 1
        line = data_file.readline()
        while line != '':
            data = line.split()
            fName = data[0]
            lName = data[1]
            type = f"({data[2]})"
            duration = data[3]

            if int(duration) > 15:
                lName += '*'

            print(f"{counter}: {fName},{lName: <15}\t{type: <15}\t{duration} minutes")

            line = data_file.readline()
            counter += 1

try:
    # open("performer.txt", "w")
    isRunning = True

    while True:
        print(Menu)
        option = int(input("==> "))
        if option == 1:
            with open("performer.txt", "a") as output:
                addPerformers()
            output.close()
        elif option == 2:
            showDetails()
        elif option == 3:
            isRunning = False
            break
        else:
            print(ErrorData)
except:
    print(ErrorData)