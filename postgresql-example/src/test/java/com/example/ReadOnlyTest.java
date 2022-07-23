package com.example;

import static org.assertj.core.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

/**
 * Read onlyの場合にSELECT/INSERT/UPDATE/DELETEがどうなるか確認する。
 *
 */
@DBRider
public class ReadOnlyTest {

	/**
	 * Read onlyな場合はINSERTできない。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void canNotExecuteInsertInAReadOnlyTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setReadOnly(true);
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("insert into readonly_example (id, val) values (?, ?)")) {
				pst.setInt(1, 1);
				pst.setString(2, "bar");

				assertThatThrownBy(() -> pst.executeUpdate())
						.isInstanceOf(SQLException.class)
						.hasMessage("ERROR: cannot execute INSERT in a read-only transaction");
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read onlyな場合はUPDATEできない。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void canNotExecuteUpdateInAReadOnlyTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setReadOnly(true);
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("update readonly_example set val = ? where id = ?")) {
				pst.setString(1, "bar");
				pst.setInt(2, 1);

				assertThatThrownBy(() -> pst.executeUpdate())
						.isInstanceOf(SQLException.class)
						.hasMessage("ERROR: cannot execute UPDATE in a read-only transaction");
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read onlyな場合はDELETEできない。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void canNotExecuteDeleteInAReadOnlyTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setReadOnly(true);
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("delete from readonly_example where id = ?")) {
				pst.setInt(1, 1);

				assertThatThrownBy(() -> pst.executeUpdate())
						.isInstanceOf(SQLException.class)
						.hasMessage("ERROR: cannot execute DELETE in a read-only transaction");
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read onlyな場合でもSELECTできる。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void seletInReadOnlyTranasction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setReadOnly(true);
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("select val from readonly_example where id = ?")) {
				pst.setInt(1, 1);
				try (ResultSet rs = pst.executeQuery()) {
					assertThat(rs.next()).isTrue();
					assertThat(rs.getString(1)).isEqualTo("foo");
					assertThat(rs.next()).isFalse();
				}
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read OnlyでなければINSERTできる。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void insertInTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("insert into readonly_example (id, val) values (?, ?)")) {
				pst.setInt(1, 2);
				pst.setString(2, "bar");

				assertThat(pst.executeUpdate()).isEqualTo(1);
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read OnlyでなければUPDATEできる。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void updateInTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("update readonly_example set val = ? where id = ?")) {
				pst.setString(1, "bar");
				pst.setInt(2, 1);

				assertThat(pst.executeUpdate()).isEqualTo(1);
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read OnlyでなければDELETEできる。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void deleteInTransaction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("delete from readonly_example where id = ?")) {
				pst.setInt(1, 1);

				assertThat(pst.executeUpdate()).isEqualTo(1);
			} finally {
				con.rollback();
			}
		}
	}

	/**
	 * Read Onlyでない場合もSELECTできる。
	 * 
	 * @throws SQLException DBアクセスで発生し得る例外
	 */
	@Test
	@DataSet("readonly_example.yml")
	void seletInTranasction() throws SQLException {
		try (Connection con = Connections.get()) {
			con.setAutoCommit(false);
			try (PreparedStatement pst = con.prepareStatement("select val from readonly_example where id = ?")) {
				pst.setInt(1, 1);
				try (ResultSet rs = pst.executeQuery()) {
					assertThat(rs.next()).isTrue();
					assertThat(rs.getString(1)).isEqualTo("foo");
					assertThat(rs.next()).isFalse();
				}
			} finally {
				con.rollback();
			}
		}
	}
}
