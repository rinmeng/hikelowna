import sys
import math


def haversine_distance(lat1, lon1, lat2, lon2):
    """
    Calculate the great circle distance between two points
    on the earth (specified in decimal degrees)
    """
    # Convert decimal degrees to radians
    lat1, lon1, lat2, lon2 = map(math.radians, [lat1, lon1, lat2, lon2])

    # Haversine formula
    dlat = lat2 - lat1
    dlon = lon2 - lon1
    a = (
        math.sin(dlat / 2) ** 2
        + math.cos(lat1) * math.cos(lat2) * math.sin(dlon / 2) ** 2
    )
    c = 2 * math.asin(math.sqrt(a))

    # Radius of earth in kilometers
    r = 6371

    # Calculate the result
    return c * r


def convert_linestring_to_latlng(linestring, reverse=False):
    # Remove the 'LINESTRING (' and ')' parts
    coords_str = linestring.replace("LINESTRING (", "").replace(")", "")

    # Split the coordinates
    coords = coords_str.split(", ")

    # Optionally reverse the coordinates
    if reverse:
        coords = coords[::-1]

    # Convert and format each coordinate pair
    latlng_lines = []

    for i in range(len(coords)):
        # Split longitude and latitude
        lon, lat = map(float, coords[i].split())

        # Format into LatLng add line
        latlng_line = f"trailPoints.add(new LatLng({lat}, {lon}));"
        latlng_lines.append(latlng_line)

    return latlng_lines


def convert_to_linestring_format(coordinates):
    """
    Convert a list of coordinates to the LineString format.
    """
    linestring = "LINESTRING ("
    linestring += ", ".join([f"{lon} {lat}" for lat, lon in coordinates])
    linestring += ")"
    return linestring


def extract_coordinates_from_latlng(lines):
    """
    Extract coordinates from LatLng lines
    """
    coordinates = []
    for line in lines:
        # Check if the line contains 'LatLng'
        if "LatLng" in line:
            # Extract the part inside parentheses
            try:
                coord_str = line.split("LatLng(")[1].split(")")[0]
                lat, lon = map(float, coord_str.split(", "))
                coordinates.append((lat, lon))
            except (ValueError, IndexError) as e:
                print(f"Skipping invalid line: {line.strip()} - {e}")
    return coordinates


def calculate_total_distance(coordinates):
    """
    Calculate total distance between consecutive coordinates
    """
    total_distance = 0
    for i in range(len(coordinates) - 1):
        lat1, lon1 = coordinates[i]
        lat2, lon2 = coordinates[i + 1]
        segment_distance = haversine_distance(lat1, lon1, lat2, lon2)
        total_distance += segment_distance
    return total_distance


def estimate_time(distance, speed_kmh):
    """
    Estimate travel time in minutes given distance (in kilometers) and speed (in km/h)
    """
    if speed_kmh <= 0:
        return None  # Invalid speed
    time_hours = distance / speed_kmh
    total_minutes = int(time_hours * 60)
    return total_minutes


# Read LINESTRING from file and process
try:
    # Ask the user if they want to skip the conversion step
    skip_conversion = (
        input("Skip conversion step and directly calculate distance? (yes/no): ")
        .strip()
        .lower()
        == "yes"
    )

    reverse_flag = False
    if len(sys.argv) > 1:
        if "-r" in sys.argv or "--reverse" in sys.argv:
            reverse_flag = True

    # Ask the user for the desired output format
    output_format = (
        input("Choose output format (L for LineString, T for Trail): ").strip().upper()
    )

    if output_format == "L":
        input_file = "trail_points.txt"  # Read from Trail format
        output_file = "trail_to_line_string.txt"  # Write to LineString format
    elif output_format == "T":
        input_file = "trail_points_LINESTRING.txt"  # Read from LineString format
        output_file = "line_string_to_trail.txt"  # Write to Trail format
    else:
        print("Invalid format choice. Please choose 'L' or 'T'.")
        sys.exit(1)

    if not skip_conversion:
        # Read from the appropriate input file
        with open(input_file, "r") as f:
            lines = f.readlines()

        # Convert based on the selected output format
        if output_format == "L":
            # Convert Trail to LineString format
            coordinates = extract_coordinates_from_latlng(lines)
            linestring_format = convert_to_linestring_format(coordinates)
            with open(output_file, "w") as f:
                f.write(linestring_format + "\n")

        elif output_format == "T":
            # Convert LineString to Trail format
            converted_points = convert_linestring_to_latlng(
                "".join(lines), reverse=reverse_flag
            )
            with open(output_file, "w") as f:
                for point in converted_points:
                    f.write(point + "\n")

        print(f"Conversion completed. Output written to {output_file}.")

    # Read the trail points for distance calculation
    with open(output_file, "r") as f:
        lines = f.readlines()

    # Extract coordinates
    if output_format == "T":
        coordinates = extract_coordinates_from_latlng(lines)
    else:
        # For LineString, we directly extract coordinates
        coordinates = extract_coordinates_from_latlng([lines])

    # Calculate total distance
    total_distance = calculate_total_distance(coordinates)

    # Average hiking speed
    speed_kmh = 3.0
    estimated_minutes = estimate_time(total_distance, speed_kmh)

    # Print results
    print(f"Total trail distance: {total_distance:.2f} kilometers")
    if estimated_minutes is not None:
        print(f"Estimated travel time: {estimated_minutes} minutes at {speed_kmh} km/h")
    else:
        print("Invalid speed. Unable to estimate travel time.")

except FileNotFoundError as e:
    print(f"Error: {e}")
except ValueError as e:
    print(f"Invalid input: {e}")
except Exception as e:
    print(f"An error occurred: {e}")
