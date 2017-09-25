package com.gmaslowski.telemetry

import akka.util.ByteString

object ByteStringReader {

  implicit class ByteStringTelemetryPacket(val byteString: ByteString) {
    def a = byteString.asByteBuffer

    def floatFromPacket(idx: Int): Float = byteString.slice(idx * 4 - 4, idx * 4).reverse.asByteBuffer.getFloat
    def byteFromPacket(idx: Int): Byte = byteString.asByteBuffer.get(idx)
  }
}

case class TelemetryData(m_time: Float,
                         m_lapTime: Float,
                         m_lapDistance: Float,
                         m_totalDistance: Float,
                         m_x: Float, // World space position
                         m_y: Float, // World space position
                         m_z: Float, // World space position
                         m_speed: Float, // !!! Speed of car in m/s !!!
                         m_xv: Float, // Velocity in world space
                         m_yv: Float, // Velocity in world space
                         m_zv: Float, // Velocity in world space
                         m_xr: Float, // World space right direction
                         m_yr: Float, // World space right direction
                         m_zr: Float, // World space right direction
                         m_xd: Float, // World space forward direction
                         m_yd: Float, // World space forward direction
                         m_zd: Float, // World space forward direction
                         m_susp_pos: Wheels, // Note: All wheel arrays have the order:
                         m_susp_vel: Wheels, // RL, RR, FL, FR
                         m_wheel_speed: Wheels,
                         m_throttle: Float,
                         m_steer: Float,
                         m_brake: Float,
                         m_clutch: Float,
                         m_gear: Float,
                         m_gforce_lat: Float,
                         m_gforce_lon: Float,
                         m_lap: Float,
                         m_engineRate: Float,
                         m_sli_pro_native_support: Float, // SLI Pro support
                         m_car_position: Float, // car race position
                         m_kers_level: Float, // kers energy left
                         m_kers_max_level: Float, // kers maximum energy
                         m_drs: Float, // 0 = off, 1 = on
                         m_traction_control: Float, // 0 (off) - 2 (high)
                         m_anti_lock_brakes: Float, // 0 (off) - 1 (on)
                         m_fuel_in_tank: Float, // current fuel mass
                         m_fuel_capacity: Float, // fuel capacity
                         m_in_pits: Float, // 0 = none, 1 = pitting, 2 = in pit area
                         m_sector: Float, // 0 = sector1, 1 = sector2, 2 = sector3
                         m_sector1_time: Float, // time of sector1 (or 0)
                         m_sector2_time: Float, // time of sector2 (or 0)
                         m_brakes_temp: Wheels, // brakes temperature (centigrade)
                         m_tyres_pressure: Wheels, // tyres pressure PSI
                         m_team_info: Float, // team ID

                         m_total_laps: Float, // total number of laps in this race
                         m_track_size: Float, // track size meters
                         m_last_lap_time: Float, // last lap time
                         m_max_rpm: Float, // cars max RPM, at which point the rev limiter will kick in
                         m_idle_rpm: Float, // cars idle RPM
                         m_max_gears: Float, // maximum number of gears
                         m_sessionType: Float, // 0 = unknown, 1 = practice, 2 = qualifying, 3 = race
                         m_drsAllowed: Float, // 0 = not allowed, 1 = allowed, -1 = invalid / unknown
                         m_track_number: Float, // -1 for unknown, 0-21 for tracks
                         m_vehicleFIAFlags: Float, // -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
                         m_era: Float, // era, 2017 (modern) or 1980 (classic)
                         m_engine_temperature: Float, // engine temperature (centigrade)
                         m_gforce_vert: Float, // vertical g-force component
                         m_ang_vel_x: Float, // angular velocity x-component
                         m_ang_vel_y: Float, // angular velocity y-component
                         m_ang_vel_z: Float, // angular velocity z-component
                         m_tyres_temperature: WheelsByte, // tyres temperature (centigrade)
                         m_tyres_wear: WheelsByte, // tyre wear percentage

                         m_tyre_compound: Byte, // compound of tyre – 0 = ultra soft, 1 = super soft, 2 = soft, 3 = medium, 4 = hard, 5 = inter, 6 = wet
                         m_front_brake_bias: Byte, // front brake bias (percentage)
                         m_fuel_mix: Byte, // fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
                         m_currentLapInvalid: Byte, // current lap invalid - 0 = valid, 1 = invalid
                         m_tyres_damage: WheelsByte, // tyre damage (percentage)
                         m_front_left_wing_damage: Byte, // front left wing damage (percentage)
                         m_front_right_wing_damage: Byte, // front right wing damage (percentage)
                         m_rear_wing_damage: Byte, // rear wing damage (percentage)
                         m_engine_damage: Byte, // engine damage (percentage)
                         m_gear_box_damage: Byte, // gear box damage (percentage)
                         m_exhaust_damage: Byte, // exhaust damage (percentage)
                         m_pit_limiter_status: Byte, // pit limiter status – 0 = off, 1 = on
                         m_pit_speed_limit: Byte, // pit speed limit in mph
                         m_session_time_left: Float, // NEW: time left in session in seconds
                         m_rev_lights_percent: Byte, // NEW: rev lights indicator (percentage)
                         m_is_spectating: Byte, // NEW: whether the player is spectating
                         m_spectator_car_index: Byte, // NEW: index of the car being spectated

                         // Car data
                         m_num_cars: Byte, // number of cars in data
                         m_player_car_index: Byte, // index of player's car in the array
                         m_car_data: Array[PacketCarData], // 20 data for all cars on track

                         m_yaw: Float, // NEW (v1.8)
                         m_pitch: Float, // NEW (v1.8)
                         m_roll: Float, // NEW (v1.8)
                         m_x_local_velocity: Float, // NEW (v1.8) Velocity in local space
                         m_y_local_velocity: Float, // NEW (v1.8) Velocity in local space
                         m_z_local_velocity: Float, // NEW (v1.8) Velocity in local space
                         m_susp_acceleration: Wheels, // NEW (v1.8) RL, RR, FL, FR
                         m_ang_acc_x: Float, // NEW (v1.8) angular acceleration x-component
                         m_ang_acc_y: Float, // NEW (v1.8) angular acceleration y-component
                         m_ang_acc_z: Float) // NEW (v1.8) angular acceleration z-component

case class PacketCarData(m_worldPosition: WorldPosition,
                         m_lastLapTime: Float,
                         m_currentLapTime: Float,
                         m_bestLapTime: Float,
                         m_sector1Time: Float,
                         m_sector2Time: Float,
                         m_lapDistance: Float,
                         m_driverId: Byte,
                         m_teamId: Byte,
                         m_carPosition: Byte,
                         m_currentLapNum: Byte,
                         m_tyreCompound: Byte,
                         m_inPits: Byte,
                         m_sector: Byte,
                         m_currentLapInvalid: Byte,
                         m_penalties: Byte)

case class WorldPosition(x: Float,
                         y: Float,
                         z: Float)

case class Wheels(rearLeft: Float,
                  rearRight: Float,
                  frontLeft: Float,
                  frontRight: Float)

case class WheelsByte(rearLeft: Byte,
                      rearRight: Byte,
                      frontLeft: Byte,
                      frontRight: Byte)